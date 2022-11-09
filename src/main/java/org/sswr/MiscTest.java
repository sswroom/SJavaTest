package org.sswr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
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
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.codec.binary.Base64;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.poi.hwpf.HWPFDocument;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.sswr.model.TestTable;
import org.sswr.util.crypto.Bcrypt;
import org.sswr.util.crypto.CertUtil;
import org.sswr.util.crypto.IntKeyHandler;
import org.sswr.util.data.DataTools;
import org.sswr.util.data.DateTimeUtil;
import org.sswr.util.data.GeometryUtil;
import org.sswr.util.data.JSONParser;
import org.sswr.util.data.SharedInt;
import org.sswr.util.data.SharedLong;
import org.sswr.util.data.StringUtil;
import org.sswr.util.data.textbinenc.Base32Enc;
import org.sswr.util.db.DBUtil;
import org.sswr.util.exporter.PEMExporter;
import org.sswr.util.io.FileStream;
import org.sswr.util.io.FileUtil;
import org.sswr.util.io.LogLevel;
import org.sswr.util.io.LogTool;
import org.sswr.util.io.MODBUSTCPMaster;
import org.sswr.util.io.MyProcess;
import org.sswr.util.io.OSInfo;
import org.sswr.util.io.PrintStreamWriter;
import org.sswr.util.io.ResourceLoader;
import org.sswr.util.io.StreamUtil;
import org.sswr.util.io.SystemInfoUtil;
import org.sswr.util.io.ZipUtil;
import org.sswr.util.io.FileStream.BufferType;
import org.sswr.util.io.FileStream.FileMode;
import org.sswr.util.io.FileStream.FileShare;
import org.sswr.util.io.device.ED538;
import org.sswr.util.math.Coord2DDbl;
import org.sswr.util.math.RectAreaDbl;
import org.sswr.util.math.unit.Distance.DistanceUnit;
import org.sswr.util.media.PageSplitter;
import org.sswr.util.media.PrintDocument;
import org.sswr.util.media.Printer;
import org.sswr.util.net.ASN1OIDInfo;
import org.sswr.util.net.DNSClient;
import org.sswr.util.net.DNSRequestAnswer;
import org.sswr.util.net.HTTPMyClient;
import org.sswr.util.net.IcmpUtil;
import org.sswr.util.net.RequestMethod;
import org.sswr.util.net.SocketFactory;
import org.sswr.util.net.TCPClient;
import org.sswr.util.net.TCPClientType;
import org.sswr.util.net.email.SMTPMessage;
import org.sswr.util.net.email.EmailMessage;
import org.sswr.util.net.email.EmailUtil;
import org.sswr.util.net.email.IMAPEmailReader;
import org.sswr.util.net.email.POP3EmailReader;
import org.sswr.util.net.email.ReceivedEmail;
import org.sswr.util.net.email.SMTPClient;
import org.sswr.util.net.email.SMTPConnType;
import org.sswr.util.net.email.SMTPEmailControl;
import org.sswr.util.office.DocUtil;
import org.sswr.util.office.PDFUtil;

import com.itextpdf.kernel.pdf.EncryptionConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.WriterProperties;
import com.itextpdf.text.pdf.PRStream;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfObject;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStream;

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
		HTTPMyClient cli = new HTTPMyClient("https://www.1823.gov.hk/common/ical/en.json", RequestMethod.HTTP_GET);
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

	public static void smtpClientTest()
	{
		String host = "";
		int port = 465;
		SMTPConnType connType = SMTPConnType.STARTTLS;
		String userName = "";
		String password = "";
		String fromName = "";
		String fromAddr = "";
		String toAddr = "";
		SMTPClient smtp = new SMTPClient(host, port, connType, new PrintStreamWriter(System.out));
		smtp.setPlainAuth(userName, password);
		SMTPMessage message = new SMTPMessage();
		message.setFrom(fromName, fromAddr);
		message.setSubject("測試中");
		message.setContent("中文測試, akfsld;jkafjka;fdsjkaf", "text/html; charset=utf-8");
		message.setSentDate(ZonedDateTime.now());
		message.addTo(null, toAddr);
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
		POP3EmailReader reader = new POP3EmailReader("127.0.0.1", 110, false, "sswroom@yahoo.com", "sswroom@yahoo.com");
		reader.open();
		Message[] messages = reader.getMessages();
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
		System.out.println("Is SMIME: "+EmailUtil.isSMIME(msg));
		ReceivedEmail email = EmailUtil.toReceivedEmail(msg);
		System.out.println(email.toString());
	}

	public static void crlTest()
	{
		X509CRL crl = CertUtil.loadCRL("/home/sswroom/Progs/Temp/20220615 EMPC CRL/eCertCA1-10CRL1.crl");
		
		System.out.println("isValid = " + CertUtil.isValid(crl));
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
		printer.endPrint(doc);
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
		TCPClient cli = new TCPClient(host, port, TCPClientType.PLAIN);
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
		try
		{
			Certificate cert = ks.getCertificate(ks.aliases().nextElement());
			PEMExporter.exportFile("/home/sswroom/Progs/Temp/exporttest.crt", CertUtil.toMyCert(cert));
		}
		catch (KeyStoreException ex)
		{
			ex.printStackTrace();
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
		EmailMessage msg = new EmailMessage()
		{
			public String getContent()
			{
				return "Testing";
			}

			public String getSubject()
			{
				return "Test subject";
			}

			public void addAttachment(String attachmentPath)
			{
			}

			public int getAttachmentCount()
			{
				return 1;
			}

			public String getAttachment(int index)
			{
				if (index == 0)
				{
					return "/home/sswroom/Progs/Temp/OCR1.jpg";
				}
				return null;
			}
		};

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
			writerProperties.setStandardEncryption(null, ownerPwd.getBytes(), EncryptionConstants.ALLOW_PRINTING, EncryptionConstants.ENCRYPTION_AES_128);
			PdfWriter pdfWriter = new PdfWriter(new FileOutputStream(destFile), writerProperties);
			PdfDocument pdfDocument = new PdfDocument(pdfReader, pdfWriter);
			pdfDocument.close();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String args[]) throws Exception
	{
		int type = 42;
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
		}
	}
}
