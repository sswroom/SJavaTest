package org.sswr;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.sswr.util.crypto.AES256;
import org.sswr.util.crypto.BlockCipher;
import org.sswr.util.crypto.HMAC;
import org.sswr.util.crypto.PBKDF2;
import org.sswr.util.crypto.SHA512;
import org.sswr.util.crypto.BlockCipher.ChainMode;

public class PBEWITHHMACSHA512ANDAES_256
{
	private byte[] iv;
	private byte[] salt;
	private BlockCipher enc;

	public PBEWITHHMACSHA512ANDAES_256(String key) throws NoSuchAlgorithmException
	{
		SecureRandom random;
		random = SecureRandom.getInstance("SHA1PRNG");
		this.salt = new byte[16];
		random.nextBytes(this.salt);

		byte []keyBuff = key.getBytes(StandardCharsets.UTF_8);
		HMAC hmac = new HMAC(new SHA512(), keyBuff, 0, keyBuff.length);
		this.enc = new AES256(PBKDF2.pbkdf2(this.salt, 1000, 32, hmac));

		random = SecureRandom.getInstance("SHA1PRNG");
		this.iv = new byte[this.enc.getDecBlockSize()];
		random.nextBytes(this.iv);

		this.enc.setChainMode(ChainMode.CBC);
		this.enc.setIV(this.iv);
	}

	public byte []decrypt(String b64String)
	{
		return decrypt(Base64.getDecoder().decode(b64String));
	}

	public byte []decrypt(byte []buff)
	{
		return this.enc.decrypt(buff, 0, buff.length, null);
	}
}
