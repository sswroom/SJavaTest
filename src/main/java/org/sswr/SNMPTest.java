package org.sswr;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.sswr.util.net.SNMPBindingItem;
import org.sswr.util.net.SNMPClient;
import org.sswr.util.net.SNMPErrorStatus;
import org.sswr.util.net.SNMPMIB;

public class SNMPTest
{
	private static void snmpMIBTest()
	{
		SNMPMIB mib = new SNMPMIB();
		StringBuilder sb = new StringBuilder();
		mib.loadFile("/home/sswroom/Progs/VCClass/MIBs/NAS-MIB", sb);
		System.out.println(sb.toString());
		System.out.println(mib.toString());
	}

	private static void snmpWalkTest()
	{
		SNMPClient cli = new SNMPClient();
		List<SNMPBindingItem> itemList = new ArrayList<SNMPBindingItem>();
		SNMPErrorStatus err;
		try
		{
			err = cli.v1Walk(InetAddress.getByName("192.168.0.110"), "public", "1.3.6.1.2.1", itemList);
			System.out.println("Error = " + err);
			int i = 0;
			int j = itemList.size();
			while (i < j)
			{
				System.out.println(itemList.get(i));
				i++;
			}
		}
		catch (UnknownHostException ex)
		{
			ex.printStackTrace();
		}
		cli.close();
	}

	public static void main(String args[])
	{
		snmpMIBTest();
		snmpWalkTest();
	}
}
