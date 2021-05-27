package org.sswr;

import java.sql.Connection;
import java.sql.SQLException;

import org.sswr.util.db.DBUtil;
import org.sswr.util.io.LogLevel;
import org.sswr.util.io.LogTool;

public class DbTest
{
	public static void main(String args[])
	{
		DBUtil.setSqlLogger(new LogTool().addPrintLog(System.out, LogLevel.RAW));
		Connection conn = DBUtil.openAccessFile("C:\\Progs\\RP Register System Ver 3.0.accdb");
		if (conn != null)
		{
			try
			{
				System.out.println("Connected");
				conn.close();
			}
			catch (SQLException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			System.out.println("Error in connecting to database");
		}
	}
}
