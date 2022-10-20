package org.sswr;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import org.sswr.util.net.MQTTStaticClient;
import org.sswr.temp.MQTTMsg;
import org.sswr.util.net.FailoverType;
import org.sswr.util.net.MQTTConn;
import org.sswr.util.net.MQTTEventHdlr;
import org.sswr.util.net.MQTTFailoverClient;
import org.sswr.util.net.MQTTPublishMessageHdlr;
import org.sswr.util.net.TCPClientType;
import org.sswr.util.net.MQTTStaticClient.ConnError;

public class MQTTTest implements MQTTEventHdlr
{
	public static void configSSL(String filePath) throws IOException, CertificateException, KeyStoreException, NoSuchAlgorithmException
	{
		String ksPwd = "changeit";
		String ksFileName = "keystore/mqtt.jks";
		CertificateFactory cFactory = CertificateFactory.getInstance("X.509");
		Certificate cert = cFactory.generateCertificate(new FileInputStream(filePath));
		KeyStore ks = KeyStore.getInstance(KeyStore.getDefaultType());
		ks.load(null, null);
		ks.setCertificateEntry("mqttsvr", cert);
		int i = ksFileName.lastIndexOf("/");
		if (i >= 0)
		{
			new File(ksFileName.substring(0, i)).mkdirs();
		}
		ks.store(new FileOutputStream(ksFileName), ksPwd.toCharArray());

		System.setProperty("javax.net.ssl.trustStore", ksFileName); 
		System.setProperty("javax.net.ssl.trustStorePassword", ksPwd);
	}

	public static void test0()
	{
		MQTTConn.publishMessage("localhost", 1883, TCPClientType.PLAIN, null, null, "Testing", "Hello");
	}

	public static void test1()
	{
		MQTTStaticClient cli;
//			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 1883, TCPClientType.PLAIN, 30, null, null, true);
//			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 1884, TCPClientType.PLAIN, 30, "ro", "readonly", true);
		System.setProperty("javax.net.ssl.trustStore","keystore.jks");
		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		cli = new MQTTStaticClient("test.mosquitto.org", 8883, TCPClientType.SSL, 30, null, null, true);
//			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 8885, TCPClientType.SSL, 30, "ro", "readonly", true);
		if (cli.getConnError() == ConnError.CONNECTED)
		{
			cli.handleEvents(new MQTTTest());
			cli.subscribe("#", null);
			cli.subscribe("/go-eCharger/#", new MQTTPublishMessageHdlr(){
				@Override
				public void onPublishMessage(String topic, byte[] buff, int buffOfst, int buffSize) {
				}
			});
			cli.subscribe("/go-eCharger/+/loc", new MQTTPublishMessageHdlr(){
				@Override
				public void onPublishMessage(String topic, byte[] buff, int buffOfst, int buffSize) {
					System.out.println("eChargerLoc Topic "+topic+" -> "+new String(buff, buffOfst, buffSize, StandardCharsets.UTF_8));		
				}
			});
				
			try
			{
				System.out.println("Connected to broker");
				//cli.publish("/test", "Testing");
				System.in.read();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			System.out.println("Error in connecting to broker: "+cli.getConnError().toString());
		}
		cli.close();
	}

	private static void test2()
	{
		MQTTFailoverClient client = new MQTTFailoverClient(FailoverType.MASTER_SLAVE);
		client.addClient(new MQTTStaticClient("gis2.ectrak.com.hk", 8901, TCPClientType.SSL, 30, "RFL_SPRING2", "RFL_SPRING2", true));
		client.subscribe("#", new MQTTMsg());
//		client.addClient(new MQTTStaticClient("localhost", 1883, TCPClientType.PLAIN, 30, null, null, true));
//		client.addClient(new MQTTStaticClient("localhost", 1884, TCPClientType.PLAIN, 30, null, null, true));
		int i = 20;
		while (i-- > 0)
		{
			//client.publish("test", ""+System.currentTimeMillis());
			try
			{
				Thread.sleep(1000);
			}
			catch (Exception e)
			{
			}
		}
		client.close();
	}

	private static void test3() throws Exception
	{
		MQTTStaticClient cli;
//			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 1883, TCPClientType.PLAIN, 30, null, null, true);
//			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 1884, TCPClientType.PLAIN, 30, "ro", "readonly", true);
//		System.setProperty("javax.net.ssl.trustStore","keystore.jks");
//		System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
		cli = new MQTTStaticClient("test.mosquitto.org", 8883, TCPClientType.SSL, 30, null, null, true);
		if (cli.getConnError() == ConnError.CONNECTED)
		{
			cli.handleEvents(new MQTTTest());
			cli.subscribe("#", null);
				
			try
			{
				System.out.println("Connected to broker");
				//cli.publish("/test", "Testing");
				System.in.read();
			}
			catch (IOException ex)
			{
				ex.printStackTrace();
			}
		}
		else
		{
			System.out.println("Error in connecting to broker: "+cli.getConnError().toString());
		}
		cli.close();
	}

	public static void main(String args[]) throws Exception
	{
		int testType = 3;
		switch (testType)
		{
		case 0:
			test0();
			break;
		case 1:
			test1();
			break;
		case 2:
			test2();
			break;
		case 3:
			test3();
			break;
		}
	}

	@Override
	public void onPublishMessage(String topic, byte[] buff, int buffOfst, int buffSize) {
		System.out.println("Topic "+topic+" -> "+new String(buff, buffOfst, buffSize, StandardCharsets.UTF_8));		
	}

	@Override
	public void onDisconnect() {
		System.out.println("Disconnected");
	}
	
}
