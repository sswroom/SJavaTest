package org.sswr;

import java.awt.Graphics2D;
import java.awt.print.PageFormat;

import org.sswr.util.media.PageOrientation;
import org.sswr.util.media.PaperSize;
import org.sswr.util.media.PrintDocument;
import org.sswr.util.media.PrintHandler;
import org.sswr.util.media.PaperSize.PaperType;

import jakarta.annotation.Nonnull;

public class MyPrintHandler implements PrintHandler
{
	public MyPrintHandler()
	{
	}
	
	@Override
	public boolean beginPrint(@Nonnull PrintDocument doc) {
		doc.setDocName("JavaPrintTest");
		return true;
	}

	@Override
	public boolean printPage(int pageNum, @Nonnull Graphics2D printPage)
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
	public boolean endPrint(@Nonnull PrintDocument doc) {
		return true;
	}

	@Override
	public int getNumberOfPages() {
		return 1;
	}

	@Override
	public PageFormat getPageFormat(int pageNum)
	{
		PaperSize paperSize = new PaperSize(PaperType.PT_A4);
		return paperSize.toPageForamt(PageOrientation.Portrait);
	}
}
