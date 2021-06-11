package org.sswr;

import org.sswr.util.net.SNMPMIB;

public class SNMPTest
{
	public static void main(String args[])
	{
		SNMPMIB mib = new SNMPMIB();
		StringBuilder sb = new StringBuilder();
		mib.loadFile("/home/sswroom/Progs/VCClass/MIBs/NAS-MIB", sb);
		System.out.println(sb.toString());
		System.out.println(mib.toString());
	}
}
