package org.sswr;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import org.sswr.util.media.ImageList;
import org.sswr.util.media.ImageUtil;

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
			if (ImageUtil.saveAsJpg(imgList.getImage(0), new FileOutputStream("/home/sswroom/Progs/photo2.jpg"), 1.0f))
			{
				System.out.println("Image exported");
			}
			else
			{
				System.out.println("Error in exporting to jpg");
			}
			if (ImageUtil.saveAsJpgBySize(imgList.getImage(0), new FileOutputStream("/home/sswroom/Progs/photo3.jpg"), 1500000, 2000000))
			{
				System.out.println("Image by size exported");
			}
			else
			{
				System.out.println("Error in exporting to jpg by size");
			}
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("File not found");
		}
	}
}
