package org.sswr.temp;

import org.sswr.util.net.MQTTPublishMessageHdlr;

public class MQTTMsg implements MQTTPublishMessageHdlr
{

	@Override
	public void onPublishMessage(String topic, byte[] buff, int buffOfst, int buffSize) {
		System.out.println("Received message from "+topic+": "+new String(buff, buffOfst, buffSize));
	}
}