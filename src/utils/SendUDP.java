package utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.LinkedList;

public class SendUDP
{
	public static String[] sendUDP()
	{
		LinkedList<String> response = new LinkedList<>();
		String host = "255.255.255.255";// �㲥��ַ
		int port = 9999;// �㲥��Ŀ�Ķ˿�
		String message = "@findServer@";// ���ڷ��͵��ַ���
		try
		{
			InetAddress adds = InetAddress.getByName(host);
			DatagramSocket ds = new DatagramSocket();
			DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(), adds, port);
			ds.send(dp);

			byte[] buf = new byte[1024];
			dp.setData(buf);
			ds.setSoTimeout(100);
			
			long startTime = System.currentTimeMillis();
			
			while (System.currentTimeMillis()-startTime<800)
			{
				ds.receive(dp);// �ȴ����գ����������״̬
				String returnText = new String(dp.getData()).trim();
				System.out.println(returnText);
				response.add(returnText);
			}
			ds.close();

		} catch (Exception e)
		{
			System.out.println("udp���ճ�ʱ");
		}
		System.out.println(response.toString());
		String[] responseArr=new String[response.size()];
		return response.toArray(responseArr);
	}

}
