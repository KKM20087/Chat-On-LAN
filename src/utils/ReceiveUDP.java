package utils;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

/**
 * ����udp�㲥
 * 
 * @author 22x
 *
 */
public class ReceiveUDP
{
	
	
	
	public static void receiveUDP()
	{
		int port = 9999;
		DatagramSocket ds = null;
		DatagramPacket dp = null;
		byte[] buf = new byte[1024];// �洢��������Ϣ
		String sendText = IPUtils.getLocalIP();
		System.out.println("����������ip"+sendText);
		try
		{
			while (true)
			{
				// �󶨶˿ڵ�
				ds = new DatagramSocket(port);
				dp = new DatagramPacket(buf, buf.length);
				System.out.println("�����㲥�˿ڴ򿪣�");
				ds.receive(dp);// �ȴ����գ����������״̬
				ds.send(new DatagramPacket(sendText.getBytes(), sendText.length(), dp.getSocketAddress()));
				System.out.println("�յ��㲥��Ϣ��" + new String(dp.getData()).trim());
				ds.close();
			}
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}


}