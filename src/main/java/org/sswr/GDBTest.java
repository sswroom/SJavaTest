package org.sswr;

import java.util.ArrayList;
import java.util.List;

import org.sswr.gdbmodel.Lamppost;
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
		List<String> names = new ArrayList<String>();
		fgdb.getTableNames(names);
		System.out.println(DataTools.toObjectString(names));
		String name = names.get(5);
		System.out.println(name);
		DBReader r = fgdb.getTableData("LAMPPOST", List.of("OBJECTID", "Shape"), 0, null, null);
		if (r != null)
		{
			int i = 10;
			while (i-- > 0 && r.readNext())
			{
				System.out.println(DataTools.toObjectString(r.getRowMap()));
			}
			r.close();
		}

		System.out.println("Testing:");
		System.out.println(DataTools.toObjectString(fgdb.loadItemsAsList(Lamppost.class, null, null, null, "objectId desc", 0, 0)));
		fgdb.close();
	}
}
