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
		System.out.println("toTS(zdt) = "+DateTimeUtil.toTimestamp(zdt));
		System.out.println(DateTimeUtil.parse("2020-12-31T16:00:00.123+00:00"));
		System.out.println("Year start = "+DateTimeUtil.toYearStart(zdt));

		int i = 1;
		while (i <= 365)
		{
			ZonedDateTime dt = zdt.withDayOfYear(i);
			System.out.println(dt+": "+((dt.getDayOfYear() - 1) / 7 + 1));
			i++;
		}
	}
}
