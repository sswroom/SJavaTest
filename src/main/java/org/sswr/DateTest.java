package org.sswr;

import java.time.ZonedDateTime;

import org.sswr.util.data.DateTimeUtil;

public class DateTest
{
	public static void main(String args[])
	{
		long t = System.currentTimeMillis();
		System.out.println("t = "+t);
		ZonedDateTime zdt = DateTimeUtil.newZonedDateTime(t);
		System.out.println("zdt = "+zdt);
		System.out.println("zdt.t = "+DateTimeUtil.getTimeMillis(zdt));
	}
}
