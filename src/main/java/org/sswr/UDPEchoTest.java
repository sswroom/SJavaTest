package org.sswr;

import java.io.IOException;
import java.net.InetAddress;

import org.sswr.util.io.LogTool;
import org.sswr.util.net.UDPPacketListener;
import org.sswr.util.net.UDPServer;

import jakarta.annotation.Nonnull;

public class UDPEchoTest implements UDPPacketListener
{
	private UDPServer svr;
	private LogTool log;

	public UDPEchoTest(int port)
	{
		this.log = new LogTool();
		this.svr = new UDPServer(null, port, null, this, this.log, "UDP:", 3, false);
	}

	public void close()
	{
		this.svr.close();
	}

	@Override
	public void udpPacketReceived(@Nonnull InetAddress addr, int port, @Nonnull byte[] buff, int ofst, int length) {
		System.out.println("Received "+length+" bytes");
		this.svr.sendTo(addr, port, buff, ofst, length);
	}

	public static void main(String args[])
	{
		UDPEchoTest test = new UDPEchoTest(3456);
		try
		{
			System.in.read();
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		test.close();
	}
}
