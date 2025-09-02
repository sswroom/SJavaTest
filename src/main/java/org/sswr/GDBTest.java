package org.sswr;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.sswr.util.data.DataTools;
import org.sswr.util.db.DBReader;
import org.sswr.util.io.FileUtil;
import org.sswr.util.io.LogLevel;
import org.sswr.util.io.LogTool;
import org.sswr.util.map.FileGDBDir;

public class GDBTest
{
	public static void main(String []args)
	{
		LogTool logger = new LogTool().addPrintLog(System.out, LogLevel.RAW);
		FileGDBDir fgdb = FileGDBDir.openDir(FileUtil.getRealPath("~/Progs/Temp/E20210522_PLIS.gdb"), logger);
		if (fgdb != null)
		{
			List<String> names;
			names = fgdb.queryTableNames(null);
			System.out.println(DataTools.toObjectString(names));
			String name;
			if (names != null)
				name = names.get(5);
			else
				name = null;
			System.out.println(name);
			DBReader r = fgdb.queryTableData(null, "LAMPPOST", List.of("OBJECTID", "Shape"), 0, 0, null, null);
			if (r != null)
			{
				int i = 10;
				while (i-- > 0 && r.readNext())
				{
					System.out.println(DataTools.toObjectString(r.getRowMap()));
				}
				r.close();
			}

			long t1 = System.currentTimeMillis();
			List<Map<String, Object>> objList = new ArrayList<Map<String, Object>>();
			r = fgdb.queryTableData(null, "LAMPPOST", null, 0, 0, null, null);
			if (r != null)
			{
				while (r.readNext())
				{
					objList.add(r.getRowMap());
				}
				r.close();
			}
			t1 = System.currentTimeMillis() - t1;
			System.out.println("Time for getRowMap = "+t1);

	//		System.out.println("Testing:");
	//		System.out.println(DataTools.toObjectString(fgdb.loadItemsAsList(Lamppost.class, null, null, null, "objectId desc", 0, 0)));
			fgdb.close();
		}
		else
		{
			System.out.println("Error in loading fgdb file");
		}
	}
}
