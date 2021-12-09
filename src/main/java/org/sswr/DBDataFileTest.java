package org.sswr;

import java.util.List;

import org.sswr.gdbmodel.LamppostData;
import org.sswr.util.db.DBDataFile;
import org.sswr.util.io.FileUtil;

public class DBDataFileTest
{
	public static void main(String []args)
	{
		String fileName = FileUtil.getRealPath("~/Progs/Temp/LamppostData2.ddf", false);
		List<LamppostData> dataList = DBDataFile.loadFile(fileName, LamppostData.class, LamppostData.getFieldOrder());
		if (dataList == null)
		{
			System.out.println("Error in readling file");
		}
		else
		{
			System.out.println(dataList.size() + " rows loaded from file");
			try
			{
				DBDataFile.saveFile(FileUtil.getRealPath("~/Progs/Temp/LamppostData.ddf", false), dataList, LamppostData.class, LamppostData.getFieldOrder());
				System.out.println("File saved");
			}
			catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}
	}
}
