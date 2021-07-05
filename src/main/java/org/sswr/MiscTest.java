package org.sswr;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import org.sswr.util.data.DataTools;
import org.sswr.util.data.DateTimeUtil;
import org.sswr.util.data.SharedLong;
import org.sswr.util.data.StringUtil;
import org.sswr.util.io.OSInfo;
import org.sswr.util.io.ResourceLoader;
import org.sswr.util.io.StreamUtil;
import org.sswr.util.net.ASN1OIDInfo;

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
		SharedLong lastModified = new SharedLong();
		InputStream stm = ResourceLoader.load(ASN1OIDInfo.class, "SNMPOIDDB.oidList.txt", lastModified);
		if (stm == null)
		{
			System.out.println("Error in loading resource");
		}
		else
		{
			System.out.println("InputStream class = " + stm.getClass().getName());
			System.out.println("Last modified = " + DateTimeUtil.newZonedDateTime(lastModified.value));
			System.out.println("Length = "+StreamUtil.getLength(stm));
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
		List<ASN1OIDInfo> objs = ResourceLoader.loadObjects(ASN1OIDInfo.class, "SNMPOIDDB.oidList.txt", null);
		System.out.println("SNMPOIDInfo = "+DataTools.toObjectString(objs));
	}

	public static void detectOS()
	{
		System.out.println(System.getProperties().toString());
		System.out.println("OS = "+OSInfo.getOSType());
	}

	public static void main(String args[])
	{
		int type = 1;
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
		case 3:
			detectOS();
			break;
		}	
	}
}
