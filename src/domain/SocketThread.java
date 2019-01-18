package domain;

import java.io.IOException;
import java.io.InputStream;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import privatechat.ui.PrivateChatUI;
import utils.FileUtils;

/**
 * �������Ӷ�socket �߳� �������Ӷ˷��͵���Ϣ
 *
 */
public class SocketThread extends Thread
{
	private NameSocket socket;

	public SocketThread(NameSocket socket)
	{
		this.socket = socket;
	}

	@Override
	public void run()
	{
		System.out.println("����ˣ���Ϊ" + socket.getName() + "���������߳�");
		ChatRoomUI.getInstance().addItemToOnLineCombo(socket.getName());
		ServerThread.getIntance().sendOnLineList();// �����ӵ����������б�
		try
		{
			InputStream is = socket.getSocket().getInputStream();
			ServerThread serverThread = ServerThread.getIntance();
			byte[] buf = null;

			while (true)
			{
				buf = new byte[1024];
				is.read(buf);
				System.out.println("������յ���Ϣ(" + new String(buf).trim() + ")");
				if (new String(buf).trim().equals(""))
				{
					throw new IOException();
				}

				String[] messages = new String(buf).trim().split("\r\n");
				if (messages[0].equals("@MessageToAll@"))
					serverThread.sendToAll(messages[1], messages[2]);
				else if (messages[0].equals("@MessageToOne@"))
				{
					if (messages[1].equals(serverThread.getServerNickName()))// ���յ��Է���˵�˽����Ϣ
					{
						System.out.println("����" + messages[2] + "��" + messages[3]);
						PrivateChatUI.newInstance(messages[2], messages[3]);
					} else // ���Ӷ˶����Ӷ˵�˽����Ϣ
						serverThread.sendToOne(messages[1], messages[2], messages[3]);
				} else if (messages[0].equals("@FileToOne@"))
				{
					if (messages[1].equals(serverThread.getServerNickName()))// ���յ��Է���˵�˽����Ϣ
					{
						System.out.println("����" + messages[2] + "�ķ����ļ�����" + messages[3] + "��С" + messages[4]);
						FileUtils.confirmRecFile(messages[2], messages[3], messages[4]);
					} else
					{
						System.out.println("����" + messages[1] + "�ķ����ļ�����" + messages[3] + "��С" + messages[4]);

						serverThread.sendFileMessage(messages[1], socket.getName(), messages[3], messages[4]);
					}
				} else if (messages[0].equals("@SendFileResponse@"))
				{
					if (messages[2].equals(serverThread.getServerNickName()))// ���յ��Է���˵��ļ��ظ���Ϣ
					{
						if (messages[1].equals("true"))
						{
							System.out.println("�Է�ͬ������ļ�������ip:" + messages[3]);
							FileUtils.sendFile(messages[3]);
						} else if (messages[1].equals("false"))
						{
							System.out.println("�Է��ܾ������ļ�");
							JOptionPane.showMessageDialog(null, "�Է��ܾ������ļ�", "��ʾ", 1);
						}
					} else
					{
						if (messages[3] == null)
							serverThread.sendFileResponse(messages[2], messages[1]);
						else
							serverThread.sendFileResponse(messages[2], messages[1] , messages[3]);
					}
				}
			}
		} catch (IOException e)
		{
			System.out.println("SocketThread.run+���Ӷ����ӶϿ�+��socket�����߳̽���");

			ChatRoomUI.getInstance().removeItemFromOnLineCombo(socket.getName());
			ServerThread serverThread = ServerThread.getIntance();
			serverThread.getSockets().remove(socket);
			serverThread.sendOnLineList();// �����ӶϿ��������б�
			// e.printStackTrace();
		}

	}
}
