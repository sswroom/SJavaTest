package org.sswr;

import java.io.FileInputStream;
import java.io.IOException;

import org.sswr.util.data.DataTools;
import org.sswr.util.io.FileUtil;
import org.sswr.util.office.XlsxValidator;

public class XlsxTest
{
	public static void main(String []args) throws IOException
	{
		String fileName = FileUtil.getRealPath("~/Progs/Temp/Test.xlsx");
		XlsxValidator xlsx = new XlsxValidator(new FileInputStream(fileName), new String[]{"ColA", "ColB", "ColC", "ColD", "ColE"});
		while (xlsx.nextRow())
		{
			System.out.println(DataTools.toObjectString(xlsx.getRowAsMap()));
		}
		xlsx.close();
	}	
}
