package org.sswr.temp;

import org.sswr.util.net.MQTTPublishMessageHdlr;

import jakarta.annotation.Nonnull;

public class MQTTMsg implements MQTTPublishMessageHdlr
{

	@Override
	public void onPublishMessage(@Nonnull String topic, @Nonnull byte[] buff, int buffOfst, int buffSize) {
		System.out.println("Received message from "+topic+": "+new String(buff, buffOfst, buffSize));
	}
}