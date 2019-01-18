package domain;

import java.io.IOException;
import java.net.Socket;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import privatechat.ui.PrivateChatUI;
import utils.FileUtils;

/**
 * ���Ӷ���Ϣ�߳�
 *
 */
public class ClientThread extends Thread
{

	private NameSocket socket;
	private static ClientThread clientThread = null;
	private String clientName;

	private ClientThread(String ip)
	{
		try
		{
			System.out.println("���ӵ���" + ip);
			socket = new NameSocket(new Socket(ip, 12345));
		} catch (Exception e)
		{
			e.printStackTrace();
			JOptionPane.showMessageDialog(null, "���Ӵ���" + e.getMessage(), "����", 0);
			System.exit(0);
		}
	}

	// ������Ϣ
	@Override
	public void run()
	{
		byte[] buf;
		while (true)
		{
			try
			{
				buf = new byte[1024];
				socket.getSocket().getInputStream().read(buf);
				System.out.println("���Ӷ��յ���Ϣ��" + new String(buf).trim() + ")");
				String[] txts = new String(buf).trim().split("\r\n");
				if (txts[0].equals("@MessageToAll@"))
				{
					ChatRoomUI.getInstance().toScreen(txts[1], txts[2]);
				} else if (txts[0].equals("@MessageToOne@"))
				{
					System.out.println("����" + txts[1] + "�ģ�" + txts[2]);
					PrivateChatUI.newInstance(txts[1], txts[2]);
				} else if (txts[0].equals("@FileToOne@") && txts[1].equals(clientName))
				{
					System.out.println("����" + txts[2] + "�ķ����ļ�����" + txts[3] + "��С" + txts[4]);
					FileUtils.confirmRecFile(txts[2], txts[3], txts[4]);
				} else if (txts[0].equals("@OnLineList@"))
				{
					System.out.println("�����б�" + txts[1]);
					updateOnlineList(txts[1]);
				} else if (txts[0].equals("@SendFileResponse@"))
				{
					if (txts[2].equals(clientName))
					{
						if (txts[1].equals("true"))
						{
							System.out.println("�Է�ͬ������ļ�������ip:" + txts[3]);
							FileUtils.sendFile(txts[3]);
						} else if (txts[1].equals("false"))
						{
							System.out.println("�Է��ܾ������ļ�");
							JOptionPane.showMessageDialog(null, "�Է��ܾ������ļ�", "��ʾ", 1);
						}
					}

				}
			} catch (IOException e)
			{
				JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "�����ѶϿ�����,���򼴽��˳�", "��ʾ", 1);
				System.exit(0);
				// e.printStackTrace();
			}
		}
	}

	// ���������б�
	public void updateOnlineList(String online)
	{
		String[] onlineList = online.split("&");
		ChatRoomUI.getInstance().getOnlineListCombo();
		for (String user : onlineList)
		{
			if (user.equals(clientName) == false)
				ChatRoomUI.getInstance().addItemToOnLineCombo(user);
		}

		ChatRoomUI.getInstance().getOnlineListCombo().removeAllItems();

		for (String user : onlineList)
		{
			if (user.equals(clientName) == false)
				ChatRoomUI.getInstance().addItemToOnLineCombo(user);
		}
	}

	public void send(String title, String message)
	{
		String txt = title + message;
		try
		{
			socket.getSocket().getOutputStream().write(txt.getBytes());
			System.out.println("���Ӷ˷�����" + message);
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	// public void toScreen(String nickName, String message)
	// {
	// ChatRoomUI.getInstance().getChatRecordTextArea().append(nickName + " �� " +
	// message + "\n");
	// int height= 99999 ;
	// Point p = new Point();
	// p.setLocation(0 ,
	// ChatRoomUI.getInstance().getChatRecordTextArea().getLineCount()*height);
	// ChatRoomUI.getInstance().getSp().getViewport().setViewPosition(p);
	// }

	public static void init(String ip)
	{
		clientThread = new ClientThread(ip);
	}

	public static ClientThread getInstance()
	{
		return clientThread;
	}

	public NameSocket getSocket()
	{
		return socket;
	}

	public String getClientName()
	{
		return clientName;
	}

	public void setClientName(String clientName)
	{
		this.clientName = clientName;
	}
}
