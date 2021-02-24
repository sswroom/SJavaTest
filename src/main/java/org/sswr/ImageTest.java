package org.sswr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.sswr.util.media.ImageList;
import org.sswr.util.media.ImageUtil;

public class ImageTest {
	public static void main(String args[])
	{
		try
		{
			ImageList img = ImageUtil.load(new FileInputStream("/home/sswroom/Progs/photo.jpg"));
			if (img == null)
			{
				System.out.println("Error in reading image");
			}
			else if (ImageUtil.saveAsJpg(img, new File("/home/sswroom/Progs/photo2.jpg"), 0.5f))
			{
				System.out.println("Image exported");
			}
			else
			{
				System.out.println("Error in exporting to jpg");
			}
		}
		catch (FileNotFoundException ex)
		{
			System.out.println("File not found");
		}
	}
}
