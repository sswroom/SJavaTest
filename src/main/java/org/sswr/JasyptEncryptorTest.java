package org.sswr;

import java.nio.charset.StandardCharsets;

import org.sswr.util.crypto.encrypt.JasyptEncryptor;
import org.sswr.util.crypto.encrypt.JasyptEncryptor.CipherAlgorithm;
import org.sswr.util.crypto.encrypt.JasyptEncryptor.KeyAlgorithm;

public class JasyptEncryptorTest
{
	public static void main(String args[])
	{
		String encPwd = "test";
		String userName = "testingasdfasdfa";
		String password = "testingafsdasdfasdf";
		String userNameEnc;
		String passwordEnc;
		byte buff[];
		try
		{
			JasyptEncryptor enc = new JasyptEncryptor(KeyAlgorithm.PBEWITHHMACSHA512, CipherAlgorithm.AES256, encPwd);
			buff = userName.getBytes(StandardCharsets.UTF_8);
			userNameEnc = enc.encryptAsB64(buff, 0, buff.length);
			buff = password.getBytes(StandardCharsets.UTF_8);
			passwordEnc = enc.encryptAsB64(buff, 0, buff.length);
			System.out.println("Username Enc = " + userNameEnc);
			System.out.println("Password Enc = " + passwordEnc);
			System.out.println("Username Dec = " + enc.decryptToString(userNameEnc));
			System.out.println("Password Dec = " + enc.decryptToString(passwordEnc));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}
}
