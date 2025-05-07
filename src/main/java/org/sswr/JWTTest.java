package org.sswr;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.sswr.util.crypto.cert.MyX509Cert;
import org.sswr.util.crypto.cert.MyX509Key;
import org.sswr.util.crypto.token.JWSignature.Algorithm;
import org.sswr.util.crypto.token.JWTHandler;
import org.sswr.util.crypto.token.JWTParam;
import org.sswr.util.crypto.token.JWToken;
import org.sswr.util.data.DataTools;
import org.sswr.util.data.JSONBase;
import org.sswr.util.data.JSONMapper;
import org.sswr.util.data.textbinenc.Base64Enc;

public class JWTTest
{
	public static void test1()
	{
		JWTHandler jwt = JWTHandler.createHMAC(Algorithm.HS256, "your-256-bit-secret".getBytes(StandardCharsets.UTF_8));
		if (jwt == null)
		{
			System.out.println("jwt setting is not supported");
			return;
		}
		JWTParam param = new JWTParam();
		param.setSubject("1234567890");
		param.setIssuedAt(1516239022);
		Map<String, String> payload;
		System.out.println(jwt.generate(Collections.singletonMap("name", "John Doe"), param));
		System.out.println("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ.3uGPWYtY_HtIcBGz4eUmTtcjZ4HnJZK9Z2uhx0Ks4n8");
		StringBuilder sbErr = new StringBuilder();
		JWToken t = JWToken.parse("eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJuYW1lIjoiSm9obiBEb2UiLCJzdWIiOiIxMjM0NTY3ODkwIiwiaWF0IjoxNTE2MjM5MDIyfQ.3uGPWYtY_HtIcBGz4eUmTtcjZ4HnJZK9Z2uhx0Ks4n8", sbErr);
		System.out.println("Token = "+DataTools.toObjectString(t));
		if (t != null)
		{
			System.out.println("Payload = "+DataTools.toObjectString(payload = t.parsePayload(param, false, sbErr)));
			if (payload == null)
			{
				System.out.println("Error = "+sbErr.toString());			
			}
			else
			{
				System.out.println("Params = "+param.toString());
				System.out.println("Payload.toJSON = " + JSONMapper.object2Json(payload));
			}
		}
		else
		{
			System.out.println("Error = "+sbErr.toString());			
		}
	}

	public static void test2()
	{
		String token = "";
		String rsaCert = "MIIC/jCCAeagAwIBAgIJAILi1z2L/f5YMA0GCSqGSIb3DQEBCwUAMC0xKzApBgNVBAMTImFjY291bnRzLmFjY2Vzc2NvbnRyb2wud2luZG93cy5uZXQwHhcNMjQwOTI2MTkyNzI3WhcNMjkwOTI2MTkyNzI3WjAtMSswKQYDVQQDEyJhY2NvdW50cy5hY2Nlc3Njb250cm9sLndpbmRvd3MubmV0MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiK9/aSUvnRV4zRKEpHK70hPNb04RBDGI5Cni7I1BGWobwH5jsek1xQ8k+7w6/qtxvBpiOi/oPLG11etjhLRTS2HFkKSLxqPIt+86sEIKbfVG1TxeLrwg5fVTiReyPKIDd0tvFFEvHc6bjGZFHZ/EvDfxPExepjaDopCYLJw6S8xFSCp9QlbKnjLLUoyIBapWeQ+tFK4MilQe7aZnssQR1vTuO+R1+zx5KQaaDzs/XbZUp7qpCsCuXoq3boZJEM3E5eZDYgVYBniDCQb1wp5JluYx78fweMYxSVRVB253PCu77ex0diPltJFte/B0FnvwARPMPzO6LGC2Jc71XTUQ0QIDAQABoyEwHzAdBgNVHQ4EFgQUYhedQ9z69v89gqGLjg3axhZbdXQwDQYJKoZIhvcNAQELBQADggEBAEvVdYEokUB9BA7Z1RRU2XpiF/aNbUdXwCCYIQvHqW3++tLI4VvreEq0OUNBiZge7WwZODHHDEzi/Q4XTqPgknIQZHKCPjuqo3r2AXXDRwctBTazUgnv3ZEfkeMjLGW0LY17sX16Rzh4HVKJiCxmEkpPqvb+fjAgyqE29rO8w52ni1hRiGj0i9Ky3lt1lpMNQVgItiZWV95XUQT2icqm5jxwe1FOoFl1YxnyGSDD/uLnkFCVoPHN+sG+V9h0HiM1SF4VWAcuTbH8w/MVf8JCYINCFMqYhOSLKOFa+zQL+75sbygL6PEKS8tB1As9tTho4GBQNRCJV1RznBTVU7hoiTw=";
		String header;
		JWToken jwt = JWToken.parse(token, null);
		if (jwt == null)
		{
			System.out.println("Error in parsing token");
			return;
		}
		else if ((header = jwt.getHeader()) == null)
		{
			System.out.println("Header is null");
			return;
		}
		else
		{
			JSONBase json = JSONBase.parseJSONStr(header);
			if (json == null)
			{
				System.out.println("Error in parsing header");
				return;
			}
			String kid = json.getValueString("kid");
			if (kid == null)
			{
				System.out.println("kid not found in header");
				return;
			}
			Base64Enc b64 = new Base64Enc();
			byte []keyBuff = b64.decodeBin(rsaCert);
			MyX509Cert cert = new MyX509Cert(kid, keyBuff, 0, keyBuff.length);
			MyX509Key key = cert.getNewPublicKey();
			if (key == null)
			{
				System.out.println("key not found in header");
				return;
			}
			if (!jwt.signatureValid(key.getASN1Buff(), 0, key.getASN1BuffSize(), key.getKeyType()))
			{
				System.out.println("Signature not valid");
				return;
			}
		}
	}

	public static void main(String args[])
	{
		int action = 1;
		switch (action)
		{
		case 0:
			test1();
			break;
		case 1:
			test2();
			break;
		}
	}
}
