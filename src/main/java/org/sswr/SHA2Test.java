package org.sswr;

import org.sswr.util.crypto.hash.SHA224;
import org.sswr.util.crypto.hash.SHA256;
import org.sswr.util.crypto.hash.SHA384;
import org.sswr.util.crypto.hash.SHA512;
import org.sswr.util.data.StringUtil;

public class SHA2Test
{
	public static void main(String args[])
	{
/*		byte buff[] = new byte[4];
		int v = 0x87654321;
		int v2;
		System.out.println("ROR32");
		int i = 1;
		while (i <= 32)
		{
			v2 = ByteTool.ror32(v, i);
			ByteTool.writeMInt32(buff, 0, v2);
			System.out.println(StringUtil.toHex(buff)+" "+ByteTool.countBit32(v2));
			i++;
		}

		long lv = 0xFEDCBA9876543210L;
		long lv2;
		System.out.println();
		System.out.println("ROR64");
		buff = new byte[8];
		i = 1;
		while (i <= 64)
		{
			lv2 = ByteTool.ror64(lv, i);
			ByteTool.writeMInt64(buff, 0, lv2);
			System.out.println(StringUtil.toHex(buff)+" "+ByteTool.countBit64(lv2));
			i++;
		}

		System.out.println();
		System.out.println("SHR64");
		buff = new byte[8];
		i = 1;
		while (i <= 64)
		{
			lv2 = ByteTool.shr64(lv, i);
			ByteTool.writeMInt64(buff, 0, lv2);
			System.out.println(StringUtil.toHex(buff)+" "+ByteTool.countBit64(lv2));
			i++;
		}*/

		SHA224 sha224 = new SHA224();
		System.out.println();
		System.out.println(sha224.getName());
		System.out.println(StringUtil.toHex(sha224.getValue()));
		System.out.println("d14a028c2a3a2bc9476102bb288234c415a2b01f828ea62ac5b3e42f");

		SHA256 sha256 = new SHA256();
		System.out.println();
		System.out.println(sha256.getName());
		System.out.println(StringUtil.toHex(sha256.getValue()));
		System.out.println("e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855");

		SHA384 sha384 = new SHA384();
		System.out.println();
		System.out.println(sha384.getName());
		System.out.println(StringUtil.toHex(sha384.getValue()));
		System.out.println("38b060a751ac96384cd9327eb1b1e36a21fdb71114be07434c0cc7bf63f6e1da274edebfe76f65fbd51ad2f14898b95b");

		SHA512 sha512 = new SHA512();
		System.out.println();
		System.out.println(sha512.getName());
		System.out.println(StringUtil.toHex(sha512.getValue()));
		System.out.println("cf83e1357eefb8bdf1542850d66d8007d620e4050b5715dc83f4a921d36ce9ce47d0d13c5d85f2b0ff8318d2877eec2f63b931bd47417a81a538327af927da3e");
	}
}
