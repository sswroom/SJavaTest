package org.sswr;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.imageio.ImageIO;

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.MetadataException;
import com.drew.metadata.exif.ExifIFD0Directory;

import org.imgscalr.Scalr;
import org.sswr.util.media.ImageList;
import org.sswr.util.media.ImageUtil;
import org.sswr.util.media.NearestNeighbourResizer;
import org.sswr.util.media.ResizeAspectRatio;
import org.sswr.util.media.Resizer;
import org.sswr.util.media.Size2D;
import org.sswr.util.media.StaticImage;

public class ImageTest {


	private static int readImageOrientation(File imageFile) throws IOException, MetadataException, ImageProcessingException
	{
		Metadata metadata = ImageMetadataReader.readMetadata(imageFile);
		Directory directory = metadata.getFirstDirectoryOfType(ExifIFD0Directory.class);
//		JpegDirectory jpegDirectory = metadata.getFirstDirectoryOfType(JpegDirectory.class);

		int orientation = 1;
		try {
			if(directory != null)
				orientation = directory.getInt(ExifIFD0Directory.TAG_ORIENTATION);

		} catch (MetadataException me) {
			System.out.println("MetadataException: readImageOrientation \nCould not get orientation");
		} catch (Exception me) {
			System.out.println("Exception: readImageOrientation \n" + me.getMessage());

		}

		return orientation;
	}

	private static void thirdPartyResize(String srcFile, String destFile, int pxSize)
	{
		BufferedImage biImage;
		BufferedImage biThumbnail;

		//2. Get Metadata
		File tmpImageFile = new File(srcFile);
		Scalr.Rotation toRotaion = null;
		FileOutputStream fos;

		int orient = 0;
		try
		{
			orient = readImageOrientation(tmpImageFile);
			//System.out.println("Image:"+ imgPart.getSubmittedFileName()+" ~~~ Orientation::: " + orient);
			if(orient == 6 ) //Up
			{
				toRotaion = Scalr.Rotation.CW_90;
				//System.out.println("Image:Rotation ::: CW_90" );
			}
			else if(orient == 3 ) //Right
			{
				toRotaion = Scalr.Rotation.CW_180;
				//System.out.println("Image:Rotation ::: CW_180" );
			}
			else if(orient == 8 ) //Down
			{
				toRotaion = Scalr.Rotation.CW_270;
				//System.out.println("Image:Rotation ::: CW_270" );
			}
			else
			{
				System.out.println("Image:Rotation ::: null ");
			}

		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return;
		}

		//3. Make BufferImage for processsssing
		try
		{
			biImage = ImageIO.read(new FileInputStream(srcFile));
			if (biImage == null)
			{
				return ;
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return ;
		} catch (Exception ex) {
			ex.printStackTrace();
			return ;
		}



		//3. Make thumbnail
		byte[] thumbnailbytes;
		{
			biThumbnail = Scalr.resize(biImage, pxSize);
			if(toRotaion != null)
			{
				biThumbnail = Scalr.rotate(biThumbnail, toRotaion, Scalr.OP_ANTIALIAS);
				System.out.println("Image: rotated BI Thumbnail");
			}
			else
			{
				System.out.println("Image: no rotation");
			}
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			try {
				ImageIO.write(biThumbnail, "jpg", baos);
			} catch (IOException ex) {
				ex.printStackTrace();
				return ;
			}
			thumbnailbytes = baos.toByteArray();
		}

		//4. Save image and thumbnail
		try
		{

			//Save Thumbnail
			fos = new FileOutputStream(destFile);
			fos.write(thumbnailbytes);
			fos.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
			return ;
		}

	}

	private static void myResize(String srcFile, String destFile, int pxSize)
	{
		try
		{
			ImageList imgList = ImageUtil.load(new FileInputStream(srcFile), srcFile);
			if (imgList == null)
			{
				System.out.println("Error in reading image");
				return;
			}
			StaticImage img = imgList.getImage(0);
			if (img == null)
			{
				System.out.println("img is null");
			}
			else
			{
				System.out.println(img.toString());
				Size2D size = Resizer.calcOutputSize(img.getWidth(), img.getHeight(), img.getPixelAspectRatio(), pxSize, pxSize, ResizeAspectRatio.SQUARE_PIXEL);
				StaticImage newImg = new NearestNeighbourResizer().resize(img, size);
				if (ImageUtil.saveAsJpg(newImg, new FileOutputStream(destFile), 1.0f))
				{
					System.out.println("Small Image exported");
				}
				else
				{
					System.out.println("Error in exporting to jpg");
				}
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String args[])
	{
		String srcFile = "/home/sswroom/Progs/Temp/insp1624622649783.jpg";
		String destFile = "/home/sswroom/Progs/photo2.jpg";
		int pxSize = 200;
		long stTime;
		long endTime;

		stTime = System.currentTimeMillis();
		myResize(srcFile, destFile, pxSize);
		endTime = System.currentTimeMillis();
		System.out.println("My Time used = "+(endTime - stTime)+"ms");

		stTime = System.currentTimeMillis();
		thirdPartyResize(srcFile, destFile, pxSize);
		endTime = System.currentTimeMillis();
		System.out.println("Third Party Time used = "+(endTime - stTime)+"ms");

//		try
//		{
			
/*			String fileName;
			//fileName = "/home/sswroom/Progs/photo.jpg";
			fileName = "/home/sswroom/Progs/Temp/insp1624622649783.jpg";
			long stTime = System.currentTimeMillis();
			ImageList imgList = ImageUtil.load(new FileInputStream(fileName));
			if (imgList == null)
			{
				System.out.println("Error in reading image");
				return;
			}
			long endTime = System.currentTimeMillis();
			System.out.println("Loading time = "+(endTime - stTime)+"ms");
			StaticImage img = imgList.getImage(0);
			System.out.println(img.toString());
			Size2D size = Resizer.calcOutputSize(img.getWidth(), img.getHeight(), img.getPixelAspectRatio(), 200, 200, ResizeAspectRatio.SQUARE_PIXEL);
			StaticImage newImg = new NearestNeighbourResizer().resize(img, size);
			if (ImageUtil.saveAsJpg(newImg, new FileOutputStream("/home/sswroom/Progs/photo2.jpg"), 1.0f))
			{
				System.out.println("Small Image exported");
			}
			else
			{
				System.out.println("Error in exporting to jpg");
			}*/
			/*
			if (ImageUtil.saveAsJpg(img, new FileOutputStream("/home/sswroom/Progs/photo2.jpg"), 1.0f))
			{
				System.out.println("Image exported");
			}
			else
			{
				System.out.println("Error in exporting to jpg");
			}
			if (ImageUtil.saveAsJpgBySize(img, new FileOutputStream("/home/sswroom/Progs/photo3.jpg"), 1500000, 2000000))
			{
				System.out.println("Image by size exported");
			}
			else
			{
				System.out.println("Error in exporting to jpg by size");
			}*/
/*		}
		catch (FileNotFoundException ex)
		{
			System.out.println("File not found");
		}*/
	}
}
