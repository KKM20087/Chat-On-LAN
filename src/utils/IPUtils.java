package utils;


import java.net.InetAddress;


public class IPUtils
{
	public static String getLocalIP()
	{
		try
		{
			String localIP = "";
			InetAddress addr = (InetAddress) InetAddress.getLocalHost();
			// ��ȡ����IP
			localIP = addr.getHostAddress().toString();

			return localIP;
		} catch (Exception e)
		{
			return null;
		}
	}

}
