package org.sswr;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sswr.util.data.DataTools;
import org.sswr.util.data.StringUtil;
import org.sswr.util.io.ResourceLoader;
import org.sswr.util.net.SNMPOIDInfo;

public class MiscTest
{
	public static void doubleTest()
	{
		double v1 = Double.parseDouble("5.0");
		double v2 = Double.parseDouble("5.02");
		System.out.println(StringUtil.fromDouble((v2 - v1) * 1000));
		double v3 = 5.0;
		double v4 = 5.02;
		System.out.println(StringUtil.fixDouble(v4 - v3));
		System.out.println(StringUtil.fixDouble((v4 - v3) * 1000.0));
	}

	public static void loadResourceTest()
	{
		InputStream stm = ResourceLoader.load(SNMPOIDInfo.class, "SNMPOIDDB.oidList.txt");
		if (stm == null)
		{
			System.out.println("Error in loading resource");
		}
		else
		{
			System.out.println("Loaded stream");
			try
			{
				stm.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void loadResourceTest2()
	{
		List<SNMPOIDInfo> objs = ResourceLoader.loadObjects(SNMPOIDInfo.class, "SNMPOIDDB.oidList.txt");
		System.out.println("SNMPOIDInfo = "+DataTools.toObjectString(objs));
	}

	public static void main(String args[])
	{
		int type = 2;
		switch (type)
		{
		case 0:
			doubleTest();
			break;
		case 1:
			loadResourceTest();
			break;
		case 2:
			loadResourceTest2();
			break;
		}	
	}
}
