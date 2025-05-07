package org.sswr;

import java.nio.charset.StandardCharsets;

import org.sswr.util.crypto.PBKDF2;
import org.sswr.util.crypto.hash.Hash;
import org.sswr.util.crypto.hash.SHA1;
import org.sswr.util.crypto.hash.HMAC;
import org.sswr.util.data.StringUtil;

public class PBKDF2Test
{
	private static void test(String pwd, String salt, int cnt, int dkLen)
	{
		byte key[] = pwd.getBytes(StandardCharsets.UTF_8);
		Hash hashFunc = new HMAC(new SHA1(), key, 0, key.length);
		System.out.println("Output =  " + StringUtil.toHex(PBKDF2.pbkdf2(salt.getBytes(StandardCharsets.UTF_8), cnt, dkLen, hashFunc)));
	}
	public static void main(String args[])
	{
		//rfc6070
		test("password", "salt", 1, 20);
		System.out.println("Expected: "+"0c60c80f961f0e71f3a9b524af6012062fe037a6");
		System.out.println();

		test("password", "salt", 2, 20);
		System.out.println("Expected: "+"ea6c014dc72d6f8ccd1ed92ace1d41f0d8de8957");
		System.out.println();

		test("password", "salt", 4096, 20);
		System.out.println("Expected: "+"4b007901b765489abead49d926f721d065a429c1");
		System.out.println();

		test("password", "salt", 16777216, 20);
		System.out.println("Expected: "+"eefe3d61cd4da4e4e9945b3d6ba2158c2634e984");
		System.out.println();

		test("passwordPASSWORDpassword", "saltSALTsaltSALTsaltSALTsaltSALTsalt", 4096, 25);
		System.out.println("Expected: "+"3d2eec4fe41c849b80c8d83662c0e44a8b291a964cf2f07038");
		System.out.println();
	}
}
