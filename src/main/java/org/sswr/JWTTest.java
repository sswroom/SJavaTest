package org.sswr;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import org.sswr.util.crypto.JWTHandler;
import org.sswr.util.crypto.JWTParam;
import org.sswr.util.crypto.JWToken;
import org.sswr.util.crypto.JWSignature.Algorithm;
import org.sswr.util.data.DataTools;
import org.sswr.util.data.JSONMapper;

public class JWTTest
{
	public static void main(String args[])
	{
		JWTHandler jwt = JWTHandler.createHMAC(Algorithm.HS256, "your-256-bit-secret".getBytes(StandardCharsets.UTF_8));
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
}
