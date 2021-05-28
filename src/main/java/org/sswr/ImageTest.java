package org.sswr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.sswr.util.media.ImageList;
import org.sswr.util.media.ImageUtil;
import org.sswr.util.media.NearestNeighbourResizer;
import org.sswr.util.media.ResizeAspectRatio;
import org.sswr.util.media.Resizer;
import org.sswr.util.media.Size2D;
import org.sswr.util.media.StaticImage;

public class ImageTest {
	public static void main(String args[])
	{
		try
		{
			ImageList imgList = ImageUtil.load(new FileInputStream("/home/sswroom/Progs/photo.jpg"));
			if (imgList == null)
			{
				System.out.println("Error in reading image");
			}
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
			}
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
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("File not found");
		}
	}
}
