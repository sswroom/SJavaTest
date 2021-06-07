package org.sswr;

import org.sswr.util.data.StringUtil;

public class PBEWITHHMACSHA512ANDAES_256Test
{
	public static void main(String args[])
	{
		try
		{
			PBEWITHHMACSHA512ANDAES_256 enc = new PBEWITHHMACSHA512ANDAES_256("testing");
			String userName = "abcd";
			String password = "abcd";
			System.out.println("Username = " + StringUtil.toHex(enc.decrypt(userName)));
			System.out.println("Password = " + StringUtil.toHex(enc.decrypt(password)));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
