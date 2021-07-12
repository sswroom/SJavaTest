package org.sswr;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;

import org.sswr.util.net.MQTTClient;
import org.sswr.util.net.MQTTConn;
import org.sswr.util.net.MQTTEventHdlr;
import org.sswr.util.net.MQTTPublishMessageHdlr;
import org.sswr.util.net.TCPClientType;
import org.sswr.util.net.MQTTClient.ConnError;

public class MQTTTest implements MQTTEventHdlr
{
	public static void test0()
	{
		try
		{
			MQTTConn.publishMessage(InetAddress.getLocalHost(), 1883, TCPClientType.PLAIN, null, null, "Testing", "Hello");
		}
		catch (UnknownHostException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void test1()
	{
		try
		{
			MQTTClient cli;
//			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 1883, TCPClientType.PLAIN, 30, null, null, true);
//			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 1884, TCPClientType.PLAIN, 30, "ro", "readonly", true);
			System.setProperty("javax.net.ssl.trustStore","keystore.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");
			cli = new MQTTClient(InetAddress.getByName("test.mosquitto.org"), 8883, TCPClientType.SSL, 30, null, null, true);
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
		catch (UnknownHostException ex)
		{
			ex.printStackTrace();
		}
	}

	public static void main(String args[])
	{
		int testType = 1;
		switch (testType)
		{
		case 0:
			test0();
			break;
		case 1:
			test1();
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
