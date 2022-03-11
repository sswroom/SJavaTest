package org.sswr;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.DayOfWeek;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Properties;
import java.util.TimeZone;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.sswr.util.crypto.Bcrypt;
import org.sswr.util.crypto.IntKeyHandler;
import org.sswr.util.data.DataTools;
import org.sswr.util.data.DateTimeUtil;
import org.sswr.util.data.JSONParser;
import org.sswr.util.data.SharedInt;
import org.sswr.util.data.SharedLong;
import org.sswr.util.data.StringUtil;
import org.sswr.util.data.textbinenc.Base32Enc;
import org.sswr.util.io.FileUtil;
import org.sswr.util.io.MyProcess;
import org.sswr.util.io.OSInfo;
import org.sswr.util.io.ResourceLoader;
import org.sswr.util.io.StreamUtil;
import org.sswr.util.io.SystemInfoUtil;
import org.sswr.util.io.ZipUtil;
import org.sswr.util.net.ASN1OIDInfo;
import org.sswr.util.net.HTTPMyClient;
import org.sswr.util.net.IcmpUtil;

public class MiscTest
{
	public static void doubleTest()
	{
		double v1 = Double.parseDouble("5.0");
		double v2 = Double.parseDouble("5.02");
		System.out.println(StringUtil.fromDouble((v2 - v1) * 1000));
		double v3 = 5.0;
		double v4 = 5.02;
		System.out.println(StringUtil.fixDouble(v4 - v3));
		System.out.println(StringUtil.fixDouble((v4 - v3) * 1000.0));
	}

	public static void loadResourceTest()
	{
		SharedLong lastModified = new SharedLong();
		InputStream stm = ResourceLoader.load(ASN1OIDInfo.class, "SNMPOIDDB.oidList.txt", lastModified);
		if (stm == null)
		{
			System.out.println("Error in loading resource");
		}
		else
		{
			System.out.println("InputStream class = " + stm.getClass().getName());
			System.out.println("Last modified = " + DateTimeUtil.newZonedDateTime(lastModified.value));
			System.out.println("Length = "+StreamUtil.getLength(stm));
			System.out.println("Loaded stream");
			try
			{
				stm.close();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void loadResourceTest2()
	{
		List<ASN1OIDInfo> objs = ResourceLoader.loadObjects(ASN1OIDInfo.class, "SNMPOIDDB.oidList.txt", null);
		System.out.println("SNMPOIDInfo = "+DataTools.toObjectString(objs));
	}

	public static void detectOS()
	{
		System.out.println(System.getProperties().toString());
		System.out.println("OS = "+OSInfo.getOSType());
	}


	public static byte[] otpDigest(byte[] secret, long currentInterval) throws NoSuchAlgorithmException, InvalidKeyException
	{
        byte[] challenge = ByteBuffer.allocate(8).putLong(currentInterval).array();
        Mac mac = Mac.getInstance("HMACSHA1");
        SecretKeySpec macKey = new SecretKeySpec(secret, "RAW");
        mac.init(macKey);
        return mac.doFinal(challenge);
	}

	public static int bytesToInt(byte[] hash)
	{
        // put selected bytes into result int
        int offset = hash[hash.length - 1] & 0xf;

        int binary = ((hash[offset] & 0x7f) << 24) |
                ((hash[offset + 1] & 0xff) << 16) |
                ((hash[offset + 2] & 0xff) << 8) |
                (hash[offset + 3] & 0xff);

        return binary % 1000000;
    }

	public static void otpTest()
	{
		String keyStr = "ZIBCVVORZW63LWLLLOT6LRAYSNSIH7AE";
		Base32Enc b32 = new Base32Enc();
		byte[] secret = b32.decodeBin(keyStr);
		System.out.println("Secret = "+StringUtil.toHex(secret));

		int interval = 30;
		Calendar calendar = GregorianCalendar.getInstance(TimeZone.getTimeZone("UTC"));
		long currentTimeSeconds = calendar.getTimeInMillis() / 1000;
		long currentInterval = (currentTimeSeconds / interval);
		System.out.println("Counter = " + currentInterval);

		try
		{
			byte out[] = otpDigest(secret, currentInterval);
			System.out.println("Hash = "+StringUtil.toHex(out));
			System.out.println("Code = "+bytesToInt(out));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void emailTest()
	{
		String smtpFrom = "";
		String toList = "";
		boolean tls = false;
		String smtpHost = "";
		Integer smtpPort = 25;
		String username = "";
		String password = "";
		Authenticator auth = null;

		Properties props = new Properties();
		if (tls)
		{
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.ssl.trust", smtpHost);
		}
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.port", (smtpPort == null)?(""+25):(""+smtpPort));
		if (username != null && username.length() > 0 && password != null && password.length() > 0)
		{
			props.put("mail.smtp.auth", "true");
			auth = new Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			};
		}


		Session session;
		if (auth != null)
		{
			session = Session.getInstance(props, auth);
		}
		else
		{
			session = Session.getInstance(props);
		}
		try
		{
			MimeMessage message = new MimeMessage(session);
			message.setSubject("測試中");
			message.setContent("中文測試, akfsld;jkafjka;fdsjkaf", "text/html; charset=utf-8");
			message.setSentDate(new Date(System.currentTimeMillis()));
			message.setFrom(new InternetAddress(smtpFrom));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toList));
			Transport.send(message);
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void keyGenTest()
	{
		int id = 123456;
		System.out.println("id = "+id);
		String key = IntKeyHandler.generate(id, 16, true);
		System.out.println("key = "+key);
		System.out.println("extracted id = "+IntKeyHandler.parseKey(key, true));
	}

	public static void bcryptTest()
	{
		Bcrypt bcrypt = new Bcrypt();
		long t1;
		long t2;
		boolean result;
		t1 = System.currentTimeMillis();
		result = bcrypt.isMatch("$2a$12$kQtGrSy5/39p96XsfTnpmuG1RiTw0KPKTSTsLuaooVr476.Ti9zcW", "admin");
		t2 = System.currentTimeMillis();
		System.out.println("Equals: "+result + ", t = "+(t2 - t1));

		BCryptPasswordEncoder bcryptpwd = new BCryptPasswordEncoder(10);
		t1 = System.currentTimeMillis();
		result = bcryptpwd.matches("admin", "$2a$12$kQtGrSy5/39p96XsfTnpmuG1RiTw0KPKTSTsLuaooVr476.Ti9zcW");
		t2 = System.currentTimeMillis();
		System.out.println("Equals2: "+result + ", t = "+(t2 - t1));

		t1 = System.currentTimeMillis();
		result = bcrypt.isMatch("$2a$12$aroG/pwwPj1tU5fl9a9pkO4rydAmkXRj/LqfHZOSnR6LGAZ.z.jwa", "ptAP\"mcg6oH.\";c0U2_oll.OKi<!ku");
		t2 = System.currentTimeMillis();
		System.out.println("Equals3: "+result + ", t = "+(t2 - t1));
	}

	public static void pingTest()
	{
		try
		{
			SharedInt respTime_us = new SharedInt();
			SharedInt ttl = new SharedInt();
			InetAddress addr = InetAddress.getByName("192.168.0.15");
			if (IcmpUtil.sendEcho(addr, respTime_us, ttl))
			{
				System.out.println("Ping success, ttl = "+ttl.value+", time = "+(respTime_us.value * 0.001)+"ms");
			}
			else
			{
				System.out.println("Error in pinging to "+addr.getHostAddress());
			}

			addr = InetAddress.getByName("::1");
			if (IcmpUtil.sendEcho(addr, respTime_us, ttl))
			{
				System.out.println("Ping success, ttl = "+ttl.value+", time = "+(respTime_us.value * 0.001)+"ms");
			}
			else
			{
				System.out.println("Error in pinging to "+addr.getHostAddress());
			}
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void httpTest()
	{
		System.out.println(HTTPMyClient.getAsString("http://127.0.0.1:12345", 200));
/*		try
		{
			HTTPMyClient cli = new HTTPMyClient("http://127.0.0.1:12345", "GET");
			cli.addHeader("User-Agent", "Test/1.0");
			cli.addHeader("Accept", "*");
			System.out.println(cli.GetRespStatus());
			System.out.println(cli.getSvrAddr());
			System.out.println("Headers");
			int i = 0;
			int j = cli.getRespHeaderCnt();
			while (i < j)
			{
				System.out.println("Header "+i+" = "+cli.getRespHeader(i));
				i++;
			}
			byte[] ret = cli.readToEnd();
			System.out.println("Reply size = "+ret.length);
			cli.close();
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}*/
	}

	public static void fileSearchTest()
	{
		List<File> files = FileUtil.search("~/Temp/log/*t/a*");
		System.out.println("Count = "+files.size());
		int i = 0;
		int j = files.size();
		while (i < j)
		{
			System.out.println(files.get(i).getPath());
			i++;
		}
	}

	public static void executeTest()
	{
		StringBuilder sb = new StringBuilder();
		int code = MyProcess.run("python", new String[]{"/home/sswroom/Progs/Temp/test.py", "param1"}, sb);
		System.out.println("Code = "+code);
		System.out.println("Message = "+sb.toString());
	}

	public static void zipTest() throws IOException, ZipException
	{
		ZipFile zip = new ZipFile(new File("/home/sswroom/Progs/Temp/Temp2.zip"));
		File dest = new File("/home/sswroom/Progs/Temp/Temp2");
		dest.mkdirs();
		if (!ZipUtil.extract(zip, dest))
		{
			System.out.println("Error in extracting the zip file");
		}
	}

	public static void calc1Test()
	{
		double angle = -0.038;
		double h = 1.5;
		double tilting = Math.tan(angle * Math.PI / 180.0) * h;
		System.out.println("Tilting = "+(-1 + tilting));
		System.out.println("Calc = "+ (Math.atan(tilting / h) * 180 / Math.PI));
	}

	public static void jsonTest() throws IOException
	{
		String fileName = FileUtil.getRealPath("~/Progs/Temp/20220311 JSON/data.json", false);
		FileInputStream fis = new FileInputStream(fileName);
		String jsonStr = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
		fis.close();
		Object o = JSONParser.parse(jsonStr);
		System.out.println(o.getClass().getName());
	}

	public static void jsonWebTest() throws IOException
	{
		HTTPMyClient cli = new HTTPMyClient("https://www.1823.gov.hk/common/ical/en.json", "GET");
		byte[] buff = cli.readToEnd();
		System.out.println(buff.length);
		String jsonStr = new String(buff, StandardCharsets.UTF_8);
		cli.close();
		Object o = JSONParser.parse(jsonStr);
		System.out.println(o.getClass().getName());
	}

	private static boolean inMonthlyRange(ZonedDateTime lastCheck, ZonedDateTime currTime, int monthlyAdj)
	{
		ZonedDateTime timeRangeFrom = DateTimeUtil.toMonthStart(currTime);
		ZonedDateTime timeRangeTo;
		while (timeRangeFrom.getDayOfWeek() != DayOfWeek.MONDAY)
		{
			timeRangeFrom = timeRangeFrom.plusDays(1);
		}
		if (timeRangeFrom.plusDays(monthlyAdj).compareTo(currTime) > 0)
		{
			timeRangeFrom = DateTimeUtil.toMonthStart(timeRangeFrom.minusMonths(1));
			while (timeRangeFrom.getDayOfWeek() != DayOfWeek.MONDAY)
			{
				timeRangeFrom = timeRangeFrom.plusDays(1);
			}
		}
		timeRangeTo = DateTimeUtil.toMonthStart(timeRangeFrom.plusMonths(1));
		while (timeRangeTo.getDayOfWeek() != DayOfWeek.MONDAY)
		{
			timeRangeTo = timeRangeTo.plusDays(1);
		}
		if (timeRangeFrom.plusDays(monthlyAdj).compareTo(lastCheck) <= 0 && timeRangeTo.plusDays(monthlyAdj).compareTo(lastCheck) > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	public static void timeCheckTest()
	{
		int monthlyAdj = 0;
		long currTime = System.currentTimeMillis();
		long t = 1639933727220L;
		System.out.println("inMonthlyRange = "+inMonthlyRange(DateTimeUtil.newZonedDateTime(t), DateTimeUtil.newZonedDateTime(currTime), monthlyAdj));
		System.out.println("inMonthlyRange = "+inMonthlyRange(DateTimeUtil.newZonedDateTime(t), ZonedDateTime.of(2022, 1, 2, 0, 0, 0, 0, ZoneId.systemDefault()), monthlyAdj));
		System.out.println("inMonthlyRange = "+inMonthlyRange(DateTimeUtil.newZonedDateTime(t), ZonedDateTime.of(2022, 1, 3, 0, 0, 0, 0, ZoneId.systemDefault()), monthlyAdj));
		System.out.println("inMonthlyRange = "+inMonthlyRange(DateTimeUtil.newZonedDateTime(t), ZonedDateTime.of(2021, 12, 6, 0, 0, 0, 0, ZoneId.systemDefault()), monthlyAdj));
	}

	public static void sysInfoTest()
	{
		System.out.println("OS Storage Free Space = "+DataTools.toObjectString(SystemInfoUtil.getFreeSpaces()));
		System.out.println("Memory status = "+DataTools.toObjectString(SystemInfoUtil.getMemoryStatus()));
		//"Memory Total / Used / Free = ", Specific Processes's CPU and memory usage
		System.out.println("Process status = "+DataTools.toObjectString(SystemInfoUtil.getProcessStatus(List.of("rpis-webapp.jar", "rpis-ctrl.jar", "web-app.jar", "api-app.jar"))));
	}

	public static void splitTest()
	{
		String data = "ADMS,,,";
		int i = 10000000;
		String[] ret = null;
		int j = 0;
		long t = System.currentTimeMillis();
		while (i-- > 0)
		{
			ret = StringUtil.split(data, ",");
//			ret = data.split(",");
			j += ret[0].length();
		}
		System.out.println("Time = "+(System.currentTimeMillis() - t));
		System.out.println("Count = "+ret.length+", "+j);
	}

	public static void main(String args[]) throws Exception
	{
		int type = 18;
		switch (type)
		{
		case 0:
			doubleTest();
			break;
		case 1:
			loadResourceTest();
			break;
		case 2:
			loadResourceTest2();
			break;
		case 3:
			detectOS();
			break;
		case 4:
			otpTest();
			break;
		case 5:
			emailTest();
			break;
		case 6:
			keyGenTest();
			break;
		case 7:
			bcryptTest();
			break;
		case 8:
			pingTest();
			break;
		case 9:
			httpTest();
			break;
		case 10:
			fileSearchTest();
			break;
		case 11:
			executeTest();
			break;
		case 12:
			zipTest();
			break;
		case 13:
			calc1Test();
			break;
		case 14:
			jsonTest();
			break;
		case 15:
			timeCheckTest();
			break;
		case 16:
			sysInfoTest();
			break;
		case 17:
			splitTest();
			break;
		case 18:
			jsonWebTest();
			break;
		}	
	}
}
