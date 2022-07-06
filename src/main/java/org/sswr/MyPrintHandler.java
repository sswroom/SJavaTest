package org.sswr;

import java.awt.Graphics2D;

import org.sswr.util.media.PrintDocument;
import org.sswr.util.media.PrintHandler;

public class MyPrintHandler implements PrintHandler
{
	public MyPrintHandler()
	{
	}
	
	@Override
	public boolean beginPrint(PrintDocument doc) {
		doc.setDocName("JavaPrintTest");
		return true;
	}

	@Override
	public boolean printPage(int pageNum, Graphics2D printPage)
	{
		if (pageNum == 0)
		{
			printPage.setClip(null);
//			printPage.setFont(new Font("Arial", Font.PLAIN, 12));
//			printPage.setColor(Color.BLACK);
			printPage.drawString("Testing", 72, 80);
			printPage.drawLine(72, 72, 272, 72);
			return true;
		}
		else
		{
			return false;
		}
	}

	@Override
	public boolean endPrint(PrintDocument doc) {
		return true;
	}
}
