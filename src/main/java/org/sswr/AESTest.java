package org.sswr;

import org.sswr.util.crypto.encrypt.AES128;
import org.sswr.util.crypto.encrypt.AES192;
import org.sswr.util.crypto.encrypt.AES256;
import org.sswr.util.crypto.encrypt.EncryptionException;
import org.sswr.util.data.StringUtil;

public class AESTest
{
	private static void testAES128() throws EncryptionException
	{
		byte key[]         = {(byte)0x2b, (byte)0x7e, (byte)0x15, (byte)0x16, (byte)0x28, (byte)0xae, (byte)0xd2, (byte)0xa6, (byte)0xab, (byte)0xf7, (byte)0x15, (byte)0x88, (byte)0x09, (byte)0xcf, (byte)0x4f, (byte)0x3c};
		byte testVector1[] = {(byte)0x6b, (byte)0xc1, (byte)0xbe, (byte)0xe2, (byte)0x2e, (byte)0x40, (byte)0x9f, (byte)0x96, (byte)0xe9, (byte)0x3d, (byte)0x7e, (byte)0x11, (byte)0x73, (byte)0x93, (byte)0x17, (byte)0x2a};
		byte testVector2[] = {(byte)0xae, (byte)0x2d, (byte)0x8a, (byte)0x57, (byte)0x1e, (byte)0x03, (byte)0xac, (byte)0x9c, (byte)0x9e, (byte)0xb7, (byte)0x6f, (byte)0xac, (byte)0x45, (byte)0xaf, (byte)0x8e, (byte)0x51};
		byte testVector3[] = {(byte)0x30, (byte)0xc8, (byte)0x1c, (byte)0x46, (byte)0xa3, (byte)0x5c, (byte)0xe4, (byte)0x11, (byte)0xe5, (byte)0xfb, (byte)0xc1, (byte)0x19, (byte)0x1a, (byte)0x0a, (byte)0x52, (byte)0xef};
		byte testVector4[] = {(byte)0xf6, (byte)0x9f, (byte)0x24, (byte)0x45, (byte)0xdf, (byte)0x4f, (byte)0x9b, (byte)0x17, (byte)0xad, (byte)0x2b, (byte)0x41, (byte)0x7b, (byte)0xe6, (byte)0x6c, (byte)0x37, (byte)0x10};
		AES128 aes = new AES128(key);
		byte cipherText[];
		byte decText[];
		
		System.out.println("AES128");
		System.out.println("Encryption key: " + StringUtil.toHex(key));
		System.out.println();

		System.out.println("Test vector: " + StringUtil.toHex(testVector1, 0, 16));
		cipherText = aes.encrypt(testVector1, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "3ad77bb40d7a3660a89ecaf32466ef97");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();

		System.out.println("Test vector: " + StringUtil.toHex(testVector2, 0, 16));
		cipherText = aes.encrypt(testVector2, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "f5d3d58503b9699de785895a96fdbaaf");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
			
		System.out.println("Test vector: " + StringUtil.toHex(testVector3, 0, 16));
		cipherText = aes.encrypt(testVector3, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "43b1cd7f598ece23881b00e3ed030688");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
			
		System.out.println("Test vector: " + StringUtil.toHex(testVector4, 0, 16));
		cipherText = aes.encrypt(testVector4, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "7b0c785e27e8ad3f8223207104725dd4");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
	}

	private static void testAES192() throws EncryptionException
	{
		byte key[]         = {(byte)0x8e, (byte)0x73, (byte)0xb0, (byte)0xf7, (byte)0xda, (byte)0x0e, (byte)0x64, (byte)0x52, (byte)0xc8, (byte)0x10, (byte)0xf3, (byte)0x2b,
							  (byte)0x80, (byte)0x90, (byte)0x79, (byte)0xe5, (byte)0x62, (byte)0xf8, (byte)0xea, (byte)0xd2, (byte)0x52, (byte)0x2c, (byte)0x6b, (byte)0x7b};
		byte testVector1[] = {(byte)0x6b, (byte)0xc1, (byte)0xbe, (byte)0xe2, (byte)0x2e, (byte)0x40, (byte)0x9f, (byte)0x96, (byte)0xe9, (byte)0x3d, (byte)0x7e, (byte)0x11, (byte)0x73, (byte)0x93, (byte)0x17, (byte)0x2a};
		byte testVector2[] = {(byte)0xae, (byte)0x2d, (byte)0x8a, (byte)0x57, (byte)0x1e, (byte)0x03, (byte)0xac, (byte)0x9c, (byte)0x9e, (byte)0xb7, (byte)0x6f, (byte)0xac, (byte)0x45, (byte)0xaf, (byte)0x8e, (byte)0x51};
		byte testVector3[] = {(byte)0x30, (byte)0xc8, (byte)0x1c, (byte)0x46, (byte)0xa3, (byte)0x5c, (byte)0xe4, (byte)0x11, (byte)0xe5, (byte)0xfb, (byte)0xc1, (byte)0x19, (byte)0x1a, (byte)0x0a, (byte)0x52, (byte)0xef};
		byte testVector4[] = {(byte)0xf6, (byte)0x9f, (byte)0x24, (byte)0x45, (byte)0xdf, (byte)0x4f, (byte)0x9b, (byte)0x17, (byte)0xad, (byte)0x2b, (byte)0x41, (byte)0x7b, (byte)0xe6, (byte)0x6c, (byte)0x37, (byte)0x10};
		AES192 aes = new AES192(key);
		byte cipherText[];
		byte decText[];

		System.out.println("AES192");
		System.out.println("Encryption key: " + StringUtil.toHex(key));
		System.out.println();

		System.out.println("Test vector: " + StringUtil.toHex(testVector1, 0, 16));
		cipherText = aes.encrypt(testVector1, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "bd334f1d6e45f25ff712a214571fa5cc");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();

		System.out.println("Test vector: " + StringUtil.toHex(testVector2, 0, 16));
		cipherText = aes.encrypt(testVector2, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "974104846d0ad3ad7734ecb3ecee4eef");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
			
		System.out.println("Test vector: " + StringUtil.toHex(testVector3, 0, 16));
		cipherText = aes.encrypt(testVector3, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "ef7afd2270e2e60adce0ba2face6444e");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
			
		System.out.println("Test vector: " + StringUtil.toHex(testVector4, 0, 16));
		cipherText = aes.encrypt(testVector4, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "9a4b41ba738d6c72fb16691603c18e0e");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
	}

	private static void testAES256() throws EncryptionException
	{
		byte key[]         = {(byte)0x60, (byte)0x3d, (byte)0xeb, (byte)0x10, (byte)0x15, (byte)0xca, (byte)0x71, (byte)0xbe, (byte)0x2b, (byte)0x73, (byte)0xae, (byte)0xf0, (byte)0x85, (byte)0x7d, (byte)0x77, (byte)0x81,
							  (byte)0x1f, (byte)0x35, (byte)0x2c, (byte)0x07, (byte)0x3b, (byte)0x61, (byte)0x08, (byte)0xd7, (byte)0x2d, (byte)0x98, (byte)0x10, (byte)0xa3, (byte)0x09, (byte)0x14, (byte)0xdf, (byte)0xf4};
		byte testVector1[] = {(byte)0x6b, (byte)0xc1, (byte)0xbe, (byte)0xe2, (byte)0x2e, (byte)0x40, (byte)0x9f, (byte)0x96, (byte)0xe9, (byte)0x3d, (byte)0x7e, (byte)0x11, (byte)0x73, (byte)0x93, (byte)0x17, (byte)0x2a};
		byte testVector2[] = {(byte)0xae, (byte)0x2d, (byte)0x8a, (byte)0x57, (byte)0x1e, (byte)0x03, (byte)0xac, (byte)0x9c, (byte)0x9e, (byte)0xb7, (byte)0x6f, (byte)0xac, (byte)0x45, (byte)0xaf, (byte)0x8e, (byte)0x51};
		byte testVector3[] = {(byte)0x30, (byte)0xc8, (byte)0x1c, (byte)0x46, (byte)0xa3, (byte)0x5c, (byte)0xe4, (byte)0x11, (byte)0xe5, (byte)0xfb, (byte)0xc1, (byte)0x19, (byte)0x1a, (byte)0x0a, (byte)0x52, (byte)0xef};
		byte testVector4[] = {(byte)0xf6, (byte)0x9f, (byte)0x24, (byte)0x45, (byte)0xdf, (byte)0x4f, (byte)0x9b, (byte)0x17, (byte)0xad, (byte)0x2b, (byte)0x41, (byte)0x7b, (byte)0xe6, (byte)0x6c, (byte)0x37, (byte)0x10};
		AES256 aes = new AES256(key);
		byte cipherText[];
		byte decText[];

		System.out.println("AES256");
		System.out.println("Encryption key: " + StringUtil.toHex(key));
		System.out.println();

		System.out.println("Test vector: " + StringUtil.toHex(testVector1, 0, 16));
		cipherText = aes.encrypt(testVector1, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "f3eed1bdb5d2a03c064b5a7e3db181f8");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();

		System.out.println("Test vector: " + StringUtil.toHex(testVector2, 0, 16));
		cipherText = aes.encrypt(testVector2, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "591ccb10d410ed26dc5ba74a31362870");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
			
		System.out.println("Test vector: " + StringUtil.toHex(testVector3, 0, 16));
		cipherText = aes.encrypt(testVector3, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "b6ed21b99ca6f4f9f153e7b1beafed1d");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
			
		System.out.println("Test vector: " + StringUtil.toHex(testVector4, 0, 16));
		cipherText = aes.encrypt(testVector4, 0, 16);
		System.out.println("Cipher text: " + StringUtil.toHex(cipherText, 0, 16));
		System.out.println("Expected:    " + "23304b7a39f9f3ff067d8d8f9e24ecc7");
		decText = aes.decrypt(cipherText, 0, 16);
		System.out.println("DecryptText: " + StringUtil.toHex(decText, 0, 16));
		System.out.println();
	}

	public static void main(String args[]) throws EncryptionException
	{
		testAES128();
		testAES192();
		testAES256();
	}
}
