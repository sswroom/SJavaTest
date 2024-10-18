package org.sswr;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.X509CRL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.sswr.model.TestTable;
import org.sswr.util.crypto.Bcrypt;
import org.sswr.util.crypto.CertUtil;
import org.sswr.util.crypto.IntKeyHandler;
import org.sswr.util.crypto.MD5;
import org.sswr.util.crypto.MyX509Cert;
import org.sswr.util.crypto.MyX509Key;
import org.sswr.util.data.DataTools;
import org.sswr.util.data.DateTimeUtil;
import org.sswr.util.data.GeometryUtil;
import org.sswr.util.data.JSONParser;
import org.sswr.util.data.SharedDouble;
import org.sswr.util.data.SharedInt;
import org.sswr.util.data.SharedLong;
import org.sswr.util.data.StringUtil;
import org.sswr.util.data.textbinenc.Base32Enc;
import org.sswr.util.data.textbinenc.EncodingException;
import org.sswr.util.db.CSVUtil;
import org.sswr.util.db.DBUtil;
import org.sswr.util.exporter.PEMExporter;
import org.sswr.util.io.FileStream;
import org.sswr.util.io.FileUtil;
import org.sswr.util.io.LogLevel;
import org.sswr.util.io.LogTool;
import org.sswr.util.io.MODBUSTCPMaster;
import org.sswr.util.io.MyProcess;
import org.sswr.util.io.OSInfo;
import org.sswr.util.io.ParserType;
import org.sswr.util.io.PrintStreamWriter;
import org.sswr.util.io.ResourceLoader;
import org.sswr.util.io.StreamUtil;
import org.sswr.util.io.SystemInfoUtil;
import org.sswr.util.io.VersionUtil;
import org.sswr.util.io.ZipUtil;
import org.sswr.util.io.FileStream.BufferType;
import org.sswr.util.io.FileStream.FileMode;
import org.sswr.util.io.FileStream.FileShare;
import org.sswr.util.io.device.ED538;
import org.sswr.util.io.stmdata.FileData;
import org.sswr.util.math.Coord2DDbl;
import org.sswr.util.math.RectAreaDbl;
import org.sswr.util.math.WKTReader;
import org.sswr.util.math.geometry.LineString;
import org.sswr.util.math.unit.Distance.DistanceUnit;
import org.sswr.util.media.PageSplitter;
import org.sswr.util.media.PrintDocument;
import org.sswr.util.media.Printer;
import org.sswr.util.net.ASN1OIDInfo;
import org.sswr.util.net.AzureManager;
import org.sswr.util.net.DNSClient;
import org.sswr.util.net.DNSRequestAnswer;
import org.sswr.util.net.HTTPClient;
import org.sswr.util.net.HTTPOSClient;
import org.sswr.util.net.IcmpUtil;
import org.sswr.util.net.RequestMethod;
import org.sswr.util.net.SSLEngine;
import org.sswr.util.net.SocketFactory;
import org.sswr.util.net.TCPClient;
import org.sswr.util.net.TCPClientType;
import org.sswr.util.net.email.SMTPMessage;
import org.sswr.util.net.email.SimpleEmailMessage;
import org.sswr.util.net.email.POP3EmailReader.ConnType;
import org.sswr.util.net.email.EmailAddress;
import org.sswr.util.net.email.EmailUtil;
import org.sswr.util.net.email.IMAPEmailReader;
import org.sswr.util.net.email.POP3EmailReader;
import org.sswr.util.net.email.ReceivedEmail;
import org.sswr.util.net.email.SMTPClient;
import org.sswr.util.net.email.SMTPConnType;
import org.sswr.util.net.email.SMTPEmailControl;
import org.sswr.util.office.DocUtil;
import org.sswr.util.office.PDFUtil;
import org.sswr.util.parser.FullParserList;
import org.sswr.util.parser.ParserList;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;

import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.Flags.Flag;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

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
		try
		{
			String key = IntKeyHandler.generate(id, 16, true);
			System.out.println("key = "+key);
			System.out.println("extracted id = "+IntKeyHandler.parseKey(key, true));
		}
		catch (EncodingException ex)
		{
			ex.printStackTrace();
		}
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

		t1 = System.currentTimeMillis();
		String hash = bcrypt.genHash(12, "ptAP\"mcg6oH.\";c0U2_oll.OKi<!ku");
		System.out.println("Gen Hash: "+hash);
		result = bcrypt.isMatch(hash, "ptAP\"mcg6oH.\";c0U2_oll.OKi<!ku");
		t2 = System.currentTimeMillis();
		System.out.println("Equals4: "+result + ", t = "+(t2 - t1));
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
		System.out.println(HTTPOSClient.getAsString("http://127.0.0.1:12345", 200));
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
		if (o == null)
		{
			System.out.println("o is null");
		}
		else
		{
			System.out.println(o.getClass().getName());
		}
	}

	public static void jsonWebTest() throws IOException
	{
		HTTPClient cli = HTTPClient.createConnect(null, null, "https://www.1823.gov.hk/common/ical/en.json", RequestMethod.HTTP_GET, true);
		byte[] buff = cli.readToEnd();
		System.out.println(buff.length);
		String jsonStr = new String(buff, StandardCharsets.UTF_8);
		cli.close();
		Object o = JSONParser.parse(jsonStr);
		if (o == null)
		{
			System.out.println("o is null");
		}
		else
		{
			System.out.println(o.getClass().getName());
		}
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
		String[] ret = {};
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

	public static void dnsListTest()
	{
		SocketFactory sockf = SocketFactory.create();
		System.out.println("DNS List: "+DataTools.toObjectString(sockf.getDefDNS()));
	}

	public static void smtpClientTest() throws Exception
	{
		String host = "";
		int port = 465;
		SMTPConnType connType = SMTPConnType.STARTTLS;
		SSLEngine ssl = new SSLEngine(false);
		String userName = "";
		String password = "";
		String fromName = "";
		String fromAddr = "";
		String toAddr = "";
		SMTPClient smtp = new SMTPClient(host, port, ssl, connType, new PrintStreamWriter(System.out));
		smtp.setPlainAuth(userName, password);
		SMTPMessage message = new SMTPMessage();
		message.setFrom(new EmailAddress(fromName, fromAddr));
		message.setSubject("測試中");
		message.setContent("中文測試, akfsld;jkafjka;fdsjkaf", "text/html; charset=utf-8");
		message.setSentDate(ZonedDateTime.now());
		message.addTo(new EmailAddress(null, toAddr));
		smtp.send(message);
	}

	public static void dnsClientTest()
	{
		SocketFactory sockf = SocketFactory.create();
		DNSClient cli = new DNSClient(sockf.getDefDNS()[0]);
		List<DNSRequestAnswer> answers = new ArrayList<DNSRequestAnswer>();
		cli.getByEmailDomainName(answers, "google.com");
		System.out.println("Result: "+DataTools.toObjectString(answers));
		cli.close();
	}

	public static void pop3Test()
	{
		POP3EmailReader reader = new POP3EmailReader("127.0.0.1", 110, ConnType.PLAIN, null, "sswroom@yahoo.com", "sswroom@yahoo.com");
		reader.open();
		Message[] messages = reader.getMessages();
		if (messages == null)
		{
			System.out.println("messages is null");
		}
		else
		{
			int i = 0;
			int j = messages.length;
			while (i < j)
			{
				try
				{
					messages[i].writeTo(System.out);
					messages[i].setFlag(Flag.DELETED, true);
				}
				catch (IOException ex)
				{
					ex.printStackTrace();
				}
				catch (MessagingException ex)
				{
					ex.printStackTrace();
				}
				i++;
			}
		}
		reader.close();
	}

	public static void imapTest()
	{
		IMAPEmailReader reader = new IMAPEmailReader("127.0.0.1", 993, true, "sswroom@yahoo.com", "sswroom@yahoo.com");
		reader.openFolder("Inbox");
/*		Message[] messages = reader.getMessages();
		int i = 0;
		int j = messages.length;
		while (i < j)
		{
			try
			{
				messages[i].writeTo(System.out);
				messages[i].setFlag(Flag.DELETED, true);
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
			catch (MessagingException ex)
			{
				ex.printStackTrace();
			}
			i++;
		}*/
		reader.close();
	}

	public static void emailFileTest()
	{
		Message msg;
		//msg = EmailUtil.loadFromEml(new File("/home/sswroom/Progs/SClass/build/Linux_dbg_x64/bin/SMTP/1653654195568.eml"));
		msg = EmailUtil.loadFromEml(new File("/home/sswroom/Progs/SClass/build/Linux_dbg_x64/bin/SMTP/1654172477245.eml"));
		if (msg == null)
		{
			System.out.println("msg is null");
		}
		else
		{
			System.out.println("Is SMIME: "+EmailUtil.isSMIME(msg));
			ReceivedEmail email = EmailUtil.toReceivedEmail(msg);
			if (email == null)
			{
				System.out.println("email is null");
			}
			else
			{
				System.out.println(email.toString());
			}
		}
	}

	public static void crlTest()
	{
		X509CRL crl = CertUtil.loadCRL("/home/sswroom/Progs/Temp/20230327 CRL Test/eCertCA2-15CRL1.crl");
		if (crl == null)
		{
			System.out.println("crl is null");
		}
		else
		{
			System.out.println("isValid = " + CertUtil.isValid(crl));
		}
	}

	public static void signTest()
	{
//		String certFile = "Certificate.cer";
//		String payloadFile = "File.PLL";
		String signFile = "File(Signed).PLL";

		byte[] sign;
		try
		{
			FileInputStream fis = new FileInputStream(signFile);
			byte[] b64data = fis.readAllBytes();
			fis.close();
			sign = new Base64().decode(b64data);
		}
		catch (IOException ex)
		{
			System.out.println("Error in reading signature file");
			ex.printStackTrace();
			return;
		}
		if (sign == null)
		{
			System.out.println("Error in reading signature");
		}
		else
		{
			System.out.println("Signature length = "+sign.length);
		}
	}

	public static void pdfTest()
	{
		String srcPDF = "/home/sswroom/Progs/Temp/ust210-83k-fl.pdf";
		String pdf2 = "/home/sswroom/Progs/Temp/LoRa gateway to network server interface definition.pdf";
		String destPDF = "/home/sswroom/Progs/Temp/pdfboxtest.pdf";
		PDDocument doc = null;
		PDDocument doc2 = null;
		System.out.println(DataTools.toObjectString(Printer.getPrinterNames()));
		try
		{
			doc = PDDocument.load(new File(srcPDF));
			doc2 = PDDocument.load(new File(pdf2));
			PDFUtil.append(doc, doc2);
			doc.save(destPDF);
			PDFUtil.close(doc);
			PDFUtil.close(doc2);
		}
		catch (IOException ex)
		{
			PDFUtil.close(doc);
			PDFUtil.close(doc2);
			ex.printStackTrace();
			return;
		}
	}

	public static void printTest()
	{
		Printer printer = new Printer("PDF");
		PrintDocument doc = printer.startPrint(new MyPrintHandler());
		if (doc == null)
		{
			System.out.println("doc is null");
		}
		else
		{
			printer.endPrint(doc);
		}
	}

	public static void printDocTest()
	{
		HWPFDocument printDoc;
		try
		{
			printDoc = new HWPFDocument(new FileInputStream(new File("/home/sswroom/Progs/Temp/printTest.doc")));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return;
		}
		printDoc.getRange();
		Printer printer = new Printer("PDF");
		DocUtil.print(printer, printDoc);
	}

	public static void keyStoreTest()
	{
		String fileName = "/etc/ssl/certs/java/cacerts";
		String password = "changeit";
		KeyStore ks = CertUtil.loadKeyStore(fileName, password);
		if (ks == null)
		{
			System.out.println("Error in loading KeyStore");
		}
		else
		{
			System.out.println("isSingleCertWithKey = " + CertUtil.isKeyStoreSingleCertWithKey(ks, password));
		}
	}

	public static void ed538Test()
	{
		String host = "192.168.1.38";
		int port = 502;
		byte addr = 0;
		int index = 2;
		TCPClient cli = new TCPClient(host, port, null, TCPClientType.PLAIN);
		if (!cli.isConnectError())
		{
			MODBUSTCPMaster modbus = new MODBUSTCPMaster(cli);
			ED538 dev = new ED538(modbus, addr);
			System.out.println("Relay status = "+dev.isRelayHigh(index));;
			System.out.println("Relay change = "+dev.setRelayState(index, true));
			System.out.println("Relay status = "+dev.isRelayHigh(index));
			System.out.println("Is recv down = "+cli.isRecvDown());
			dev.close();
		}
		else
		{
			System.out.println("Error in connecting to ED538");
		}
	}

	public static void timeTest()
	{
		Timestamp ts = DateTimeUtil.timestampNow();
		Timestamp ts2 = new Timestamp(System.currentTimeMillis());
		ZonedDateTime zdt = ZonedDateTime.now();
		System.out.println(DateTimeUtil.toString(ts, "yyyy-MM-dd HH:mm:ss.fffffffff"));
		System.out.println(DateTimeUtil.toString(ts2, "yyyy-MM-dd HH:mm:ss.fffffffff"));
		System.out.println(DateTimeUtil.toString(zdt, "yyyy-MM-dd HH:mm:ss.fffffffff"));
		System.out.println(ts.getTime() + " "+ts.getNanos());
	}

	public static void postgresqlTest()
	{
		LogTool logTool = new LogTool();
		logTool.addPrintLog(System.out, LogLevel.RAW);
		DBUtil.setSqlLogger(logTool);
		String url = "jdbc:postgresql://localhost:5432/test";
		try
		{
			Connection conn = DriverManager.getConnection(url, "postgres", "postgres");
			System.out.println("Conn Type = "+DBUtil.connGetDBType(conn));
			Map<Integer, TestTable> data = DBUtil.loadItems(TestTable.class, conn, null, null);
			System.out.println("Data: "+DataTools.toObjectString(data));
		}
		catch (SQLException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void deleteFileTest()
	{
		String path = "/home/sswroom/Progs/Temp/testdelete";
		System.out.println("delete = "+FileUtil.deleteFileOrDir(path));
	}

	public static void keyStoreErrorTest()
	{
		String fileName = "/home/sswroom/Progs/Temp/certerror.csv";
		String password = "changeit";
		KeyStore ks = CertUtil.loadKeyStore(fileName, password);
		if (ks == null)
		{
			System.out.println("Error in loading KeyStore");
			System.out.println("Deleting the file = "+FileUtil.deleteFileOrDir(fileName));
		}
		else
		{
			System.out.println("isSingleCertWithKey = " + CertUtil.isKeyStoreSingleCertWithKey(ks, password));
		}
	}

	public static void certExportTest()
	{
		KeyStore ks = CertUtil.loadDefaultTrustStore();
		if (ks == null)
		{
			System.out.println("Error in loading trust store");
		}
		else
		{
			try
			{
				Certificate cert = ks.getCertificate(ks.aliases().nextElement());
				MyX509Cert myCert = CertUtil.toMyCert(cert);
				if (myCert == null)
				{
					System.out.println("Error in converting to myCert");
				}
				else
				{
					PEMExporter.exportFile("/home/sswroom/Progs/Temp/exporttest.crt", myCert);
				}
			}
			catch (KeyStoreException ex)
			{
				ex.printStackTrace();
			}
		}
	}

	public static void geomDistTest()
	{
		PrecisionModel pm = new PrecisionModel();
		GeometryFactory gf = new GeometryFactory(pm, 2326);
		Coordinate coords[] = new Coordinate[4];
		coords[0] = new Coordinate(0, 0);
		coords[1] = new Coordinate(1, 0);
		coords[2] = new Coordinate(0.5, Math.sin(60 * Math.PI / 180));
		coords[3] = new Coordinate(0, 0);
		Polygon pg = gf.createPolygon(coords);
		System.out.println("Distance = "+GeometryUtil.calcMaxDistanceFromCenter(pg, DistanceUnit.Meter)+", Centroid = "+pg.getCentroid().toString());
	}

	public static void pdfExtractTest()
	{
		String pdfFile = "/home/sswroom/Progs/Temp/20221012 PDF Resize/pdf_files/81222.pdf";
		try
		{
			PdfReader reader = new PdfReader(pdfFile);
			int nObj = reader.getXrefSize();
			int i = 0;
			while (i < nObj)
			{
				PdfObject pdfobj = reader.getPdfObject(i);
				if (pdfobj != null && pdfobj instanceof PdfStream)
				{
					PdfStream stm = (PdfStream)pdfobj;
					PdfObject pdfsubtype = stm.get(PdfName.SUBTYPE);
					if (pdfsubtype != null && pdfsubtype.toString().equals(PdfName.IMAGE.toString()))
					{
						byte[] img = PdfReader.getStreamBytesRaw((PRStream) stm);
						FileOutputStream out = new FileOutputStream(new File(pdfFile +"."+ i + ".jpg"));
						out.write(img);
						out.flush();
						out.close();
					}
				}
				i++;
			}
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void pageSplitterTest()
	{
		PageSplitter splitter = new PageSplitter();
		RectAreaDbl area = new RectAreaDbl(new Coord2DDbl(100, 100), new Coord2DDbl(1000, 500));
		System.out.println(DataTools.toObjectString(splitter.splitDrawings(area, 30, 10, 1000)));
	}

	public static void fileTimeTest()
	{
		FileStream fs = new FileStream("/home/sswroom/Progs/Temp/photo2.jpg", FileMode.ReadOnly, FileShare.DenyNone, BufferType.Normal);
		System.out.println("Create Time: "+fs.getCreateTime());
		System.out.println("Modify Time: "+fs.getModifyTime());
		fs.close();
	}

	public static void smtpDirectControlTest()
	{
		LogTool logger = new LogTool();
		logger.addPrintLog(System.out, LogLevel.RAW);
//		SMTPDirectEmailControl ctrl = new SMTPDirectEmailControl("127.0.0.1", null, SMTPConnType.PLAIN, "test", "test", "sswroom@yahoo.com", logger);
		SMTPEmailControl ctrl = new SMTPEmailControl("127.0.0.1", null, false, "test", "test", "sswroom@yahoo.com", logger);
		SimpleEmailMessage msg = new SimpleEmailMessage("Test subject", "Testing", false);
		msg.addAttachmentFile("/home/sswroom/Progs/Temp/OCR1.jpg");
		ctrl.sendMail(msg, "sswroom@yahoo.com", null);
		System.exit(0);
	}

	public static void pdfEncTest()
	{
		String srcFile = "/home/sswroom/Progs/Temp/testPDF.pdf";
		String destFile = "/home/sswroom/Progs/Temp/testEncPDF.pdf";
		String userPWd = "user";
		String ownerPwd = "owner";
		try
		{
			com.itextpdf.kernel.pdf.PdfReader pdfReader = new com.itextpdf.kernel.pdf.PdfReader(srcFile);
			WriterProperties writerProperties = new WriterProperties();
			writerProperties.setStandardEncryption(userPWd.getBytes(), ownerPwd.getBytes(), EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.ENCRYPTION_AES_128);
			PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(destFile), writerProperties);
			PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);
			pdfDocument.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void fileCopyTest()
	{
		if (FileUtil.copyDir(new File("/home/sswroom/Progs/Temp/kmlTest"), "/home/sswroom/Progs/Temp/kmlTestCopy", true))
		{
			System.out.println("Copied");
		}
		else
		{
			System.out.println("Error in copying");
		}
	}

	public static void csvFileTest()
	{
		String fileName = "/home/sswroom/Progs/Temp/20221116 CAD/dataProcessed/adsb_movement_statistic/ADSB_Movement_Statistic_201904.csv";
		try
		{
			Instant startTime = Instant.now();
			BufferedReader reader = new BufferedReader(new FileReader(fileName));
			while (CSVUtil.readLine(reader) != null)
			{
				
			}
			reader.close();
			System.out.println("Time used: "+DateTimeUtil.timeDiffSec(Instant.now(), startTime));
		}
		catch (Exception ex)
		{
			ex.printStackTrace();
		}
	}

	public static void verTest()
	{
		System.out.println("Version = "+VersionUtil.getFileVersion(MiscTest.class));
	}

	public static void angle3dTest()
	{
		SharedDouble hAngle = new SharedDouble();
		SharedDouble vAngle = new SharedDouble();
		GeometryUtil.calcHVAngleDeg(new Coord2DDbl(114, 24), new Coord2DDbl(114, 24.1), 0, 10, hAngle, vAngle);
		System.out.println("HAngle = "+hAngle.value +", VAngle = "+vAngle.value);
		GeometryUtil.calcHVAngleDeg(new Coord2DDbl(114, 24), new Coord2DDbl(114.1, 24), 0, 20, hAngle, vAngle);
		System.out.println("HAngle = "+hAngle.value +", VAngle = "+vAngle.value);
		GeometryUtil.calcHVAngleDeg(new Coord2DDbl(114, 24), new Coord2DDbl(114, 23.9), 0, -10, hAngle, vAngle);
		System.out.println("HAngle = "+hAngle.value +", VAngle = "+vAngle.value);
		GeometryUtil.calcHVAngleDeg(new Coord2DDbl(114, 24), new Coord2DDbl(113.9, 24), 0, -20, hAngle, vAngle);
		System.out.println("HAngle = "+hAngle.value +", VAngle = "+vAngle.value);
	}

	public static void wktReaderTest()
	{
		String wkt = "LINESTRING (12643904.589899998 2536653.7993, 12678884.532000002 2548834.3268)";
		WKTReader reader = new WKTReader(3857);
		LineString pl = (LineString)reader.parseWKT(wkt.getBytes());
		System.out.println("pl = "+pl);
	}

	public static void toVector2DTest()
	{
		Coordinate coords[] = new Coordinate[2];
		coords[0] = new Coordinate(114, 24);
		coords[1] = new Coordinate(114.1, 24.1);
		GeometryFactory factory = new GeometryFactory();
		org.locationtech.jts.geom.LineString ls1 = factory.createLineString(coords);
		System.out.println("LineString = "+GeometryUtil.toVector2D(ls1));
	}

	public static void contTypeTest()
	{
		System.out.println(URLConnection.guessContentTypeFromName("Test.doc"));
	}

	public static MultiLineString parsePLL(String file)
	{
		try
		{
			BufferedReader reader = new BufferedReader(new FileReader(file));
			CSVUtil.readLine(reader);
			String[] cols;
			GeometryFactory gf = new GeometryFactory(new PrecisionModel(), 2326);
			int i;
			int j;
			List<org.locationtech.jts.geom.LineString> lsList = new ArrayList<org.locationtech.jts.geom.LineString>();
			while ((cols = CSVUtil.readLine(reader)) != null)
			{
				i = 1;
				j = cols.length;
				Coordinate[] coords = new Coordinate[j - 1];
				while (i < j)
				{
					if (!cols[i].startsWith("(") || !cols[i].endsWith(")"))
					{
						reader.close();
						System.out.println("Format error 1: "+cols[i]);
						return null;
					}
					String pt[] = StringUtil.split(cols[i].substring(1, cols[i].length() - 1), ",");
					if (pt.length != 2)
					{
						reader.close();
						System.out.println("Format error 2: "+cols[i]);
						return null;
					}
					Double x = StringUtil.toDouble(pt[0]);
					Double y = StringUtil.toDouble(pt[1]);
					if (x == null || y == null)
					{
						reader.close();
						System.out.println("Format error 3: "+cols[i]);
						return null;
					}
					coords[i - 1] = new Coordinate(x, y);
					i++;
				}
				lsList.add(gf.createLineString(coords));
			}
			reader.close();
			org.locationtech.jts.geom.LineString[] lsArray = new org.locationtech.jts.geom.LineString[lsList.size()];
			i = lsList.size();
			while (i-- > 0)
			{
				lsArray[i] = lsList.get(i);
			}
			return gf.createMultiLineString(lsArray);
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
			return null;
		}
	}

	public static void pllParseTest()
	{
		String file = "/home/sswroom/Progs/Temp/20230317 PLL Insert SQLServer error/EMPf5624.PLL";
		System.out.println(parsePLL(file).toString());
	}

	public static void emailValidTest()
	{
		String email = "sswroom@yahoo.com";
		System.out.println("Is email address = "+StringUtil.isEmailAddress(email));
	}

	public static void dateTimeToString()
	{
		System.out.println(DateTimeUtil.toString(DateTimeUtil.timestampNow(), "yyyy-MM-dd'T'HH:mm:ss.fffffffffzzzz"));
	}

	public static void azureTest() throws Exception
	{
		SocketFactory sockf = SocketFactory.create();
		SSLEngine ssl = new SSLEngine(false);
		AzureManager azure = new AzureManager(sockf, ssl);
		MyX509Key key = azure.createKey("-KI3Q9nNR7bRofxmeZoXqbHZGew");
		if (key == null)
		{
			System.out.println("Key not found");
		}
		else
		{
			StringBuilder sb = new StringBuilder();
			key.toASN1String(sb);
			System.out.println(sb.toString());
		}
	}

	public static void httpFilePostTest() throws Exception
	{
//		HTTPMyClient cli = new HTTPMyClient("http://127.0.0.1:12345/test/file", RequestMethod.HTTP_POST);
//		SSLEngine.ignoreCertCheck();
		HTTPClient cli = HTTPClient.createConnect(null, null, "https://127.0.0.1:8448/test/file", RequestMethod.HTTP_POST, true);
		cli.formBegin(true);
		cli.formAdd("abc", "def");
		cli.formAddFile("file", new File("/home/sswroom/Progs/Temp/7gogo.jpg"));
		System.out.println("Status = "+cli.getRespStatus());
		System.out.println("Resp = "+new String(cli.readToEnd(), StandardCharsets.UTF_8));
	}
	
	public static void md5Test()
	{
		byte[] testBlock = "The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.UTF_8);
		MD5 hash = new MD5();
		System.out.println(StringUtil.toHex(hash.getValue()));
		System.out.println("D41D8CD98F00B204E9800998ECF8427E");
		System.out.println();
		hash.calc(testBlock);
		System.out.println(StringUtil.toHex(hash.getValue()));
		System.out.println("9E107D9D372BB6826BD81D3542A419D6");
		System.out.println();
		hash.calc(testBlock);
		System.out.println(StringUtil.toHex(hash.getValue()));
		System.out.println("D27C6D8BCAA695E377D32387E115763C");
		System.out.println();
		hash.calc(testBlock);
		System.out.println(StringUtil.toHex(hash.getValue()));
		System.out.println("4E67DB4A7A406B0CFDADD887CDE7888E");
	}

	public static void clearMonthTest()
	{
		System.out.println(DateTimeUtil.toString(DateTimeUtil.clearDayOfMonth(DateTimeUtil.timestampNow())));
	}

	public static void utf16LengthTest()
	{
		String s = "98765432";
		System.out.println(s +": length = "+s.length()+", length16 = "+StringUtil.utf16CharCnt(s));
		byte[] bytes = s.getBytes(StandardCharsets.UTF_16);
		System.out.println(StringUtil.toHex(bytes));
	}

	public static void jpgParseTest()
	{
		String fileName = "test.JPG";
		ParserList parsers = new FullParserList();
		FileData fd = new FileData(fileName, false);
		if (parsers.parseFileType(fd, ParserType.ImageList) != null)
		{
			System.out.println("Parse success");
		}
		else
		{
			System.out.println("Parse failed");
		}
		fd.close();
	}

	public static void main(String args[]) throws Exception
	{
		int type = 58;
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
		case 19:
			dnsListTest();
			break;
		case 20:
			smtpClientTest();
			break;
		case 21:
			dnsClientTest();
			break;
		case 22:
			pop3Test();
			break;
		case 23:
			imapTest();
			break;
		case 24:
			emailFileTest();
			break;
		case 25:
			crlTest();
			break;
		case 26:
			signTest();
			break;
		case 27:
			pdfTest();
			break;
		case 28:
			printTest();
			break;
		case 29:
			printDocTest();
			break;
		case 30:
			keyStoreTest();
			break;
		case 31:
			ed538Test();
			break;
		case 32:
			timeTest();
			break;
		case 33:
			postgresqlTest();
			break;
		case 34:
			deleteFileTest();
			break;
		case 35:
			keyStoreErrorTest();
			break;
		case 36:
			certExportTest();
			break;
		case 37:
			geomDistTest();
			break;
		case 38:
			pdfExtractTest();
			break;
		case 39:
			pageSplitterTest();
			break;
		case 40:
			fileTimeTest();
			break;
		case 41:
			smtpDirectControlTest();
			break;
		case 42:
			pdfEncTest();
			break;
		case 43:
			fileCopyTest();
			break;
		case 44:
			csvFileTest();
			break;
		case 45:
			verTest();
			break;
		case 46:
			angle3dTest();
			break;
		case 47:
			wktReaderTest();
			break;
		case 48:
			toVector2DTest();
			break;
		case 49:
			contTypeTest();
			break;
		case 50:
			pllParseTest();
			break;
		case 51:
			emailValidTest();
			break;
		case 52:
			dateTimeToString();
			break;
		case 53:
			azureTest();
			break;
		case 54:
			httpFilePostTest();
			break;
		case 55:
			md5Test();
			break;
		case 56:
			clearMonthTest();
			break;
		case 57:
			utf16LengthTest();
			break;
		case 58:
			jpgParseTest();
			break;
		}
	}
}
