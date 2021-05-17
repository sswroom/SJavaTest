package org.sswr;

import org.sswr.util.data.StringUtil;

public class MiscTest
{
	public static void main(String args[])
	{
		double v1 = Double.parseDouble("5.0");
		double v2 = Double.parseDouble("5.02");
		System.out.println(StringUtil.fromDouble((v2 - v1) * 1000));
		double v3 = 5.0;
		double v4 = 5.02;
		System.out.println(StringUtil.fixDouble(v4 - v3));
		System.out.println(StringUtil.fixDouble((v4 - v3) * 1000.0));
	}
}
