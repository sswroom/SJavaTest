package org.sswr;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.sswr.util.net.MQTTClient;

public class MQTTTest
{
	public static void main(String args[])
	{
		try
		{
			MQTTClient.publishMessage(InetAddress.getLocalHost(), 1883, null, null, "Testing", "Hello");
		}
		catch (UnknownHostException ex)
		{
			ex.printStackTrace();
		}
	}	
}
