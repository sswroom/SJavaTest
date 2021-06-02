package org.sswr;

import org.sswr.util.crypto.SHA1;
import org.sswr.util.data.StringUtil;

public class SHA1Test
{
	public static void main(String args[])
	{
		SHA1 sha1 = new SHA1();
		System.out.println(StringUtil.toHex(sha1.getValue()));
		System.out.println("da39a3ee5e6b4b0d3255bfef95601890afd80709");
	}
}
