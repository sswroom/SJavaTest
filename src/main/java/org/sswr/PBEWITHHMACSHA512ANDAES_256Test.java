package org.sswr;

import org.sswr.util.crypto.JasyptEncryptor;
import org.sswr.util.crypto.JasyptEncryptor.CipherAlgorithm;
import org.sswr.util.crypto.JasyptEncryptor.KeyAlgorithm;

public class PBEWITHHMACSHA512ANDAES_256Test
{
	public static void main(String args[])
	{
		String encPwd = "test";
		String userName = "testing";
		String password = "testing";
		try
		{
			JasyptEncryptor enc = new JasyptEncryptor(KeyAlgorithm.PBEWITHHMACSHA512, CipherAlgorithm.AES256, encPwd);
			System.out.println("Username = " + enc.decryptToString(userName));
			System.out.println("Password = " + enc.decryptToString(password));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
