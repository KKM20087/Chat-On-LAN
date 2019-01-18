package domain;


import java.util.LinkedList;



import chatroom.ui.ChatRoomUI;
import utils.ListenPort;
import utils.ReceiveUDP;

/**
 * ������߳�
 * 
 * @author 22x
 *
 */
public class ServerThread extends Thread
{
	private LinkedList<NameSocket> sockets = new LinkedList<>(); // ���Ӷ��б�
	private String serverNickName;// ������ǳ�

	private static ServerThread serverThread = null;

	public static boolean refreshFlag = true;

	private ServerThread()
	{

	}

	public void setNickName(String name)
	{
		this.serverNickName = name;
	}

	public static ServerThread getIntance()
	{
		if (serverThread == null)
			ServerThread.serverThread = new ServerThread();

		return ServerThread.serverThread;
	}

	@Override
	public void run()
	{
		// �����˿ڣ���������
		new Thread()
		{
			@Override
			public void run()
			{
				ListenPort.listen();
			}
		}.start();

//		// �����б����߳�
//		new Thread()
//		{
//			@Override
//			public void run()
//			{
//				
//			}
//		}.start();

		// ����UDP�㲥
		new Thread()
		{
			@Override
			public void run()
			{
				ReceiveUDP.receiveUDP();
			}
		}.start();
	}

	/**
	 * Ⱥ����Ϣ
	 * 
	 * @param message
	 *            ��Ϣ����
	 */
	public void sendToAll(String fromUser, String message)
	{
		String forword = "@MessageToAll@\r\n" + fromUser + "\r\n" + message;
		for (NameSocket socket : sockets)
		{
			socket.sendMessage(forword);
		}
		ChatRoomUI.getInstance().toScreen(fromUser, message);
	}

	/**
	 * ˽����Ϣ
	 * 
	 * @param toUser
	 *            �� �û�
	 * @param fromUser
	 *            �����û�
	 * @param message
	 *            ��Ϣ����
	 */
	public void sendToOne(String toUser, String fromUser, String message)
	{

		String forword = "@MessageToOne@\r\n" + fromUser + "\r\n" + message;
		System.out.println("����˽�ĵ���Ϣ" + forword);
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				return;
			}
		}

	}

	/**
	 * �����ļ�ǰ������
	 * 
	 * @param toUser
	 *            ���û�
	 * @param fromUser
	 *            ��֮�û�
	 * @param fileName
	 *            �ļ���
	 * @param fileSize
	 *            �ļ���С
	 */
	public void sendFileMessage(String toUser, String fromUser, String fileName, String fileSize)
	{
		String forword = "@FileToOne@\r\n" + toUser + "\r\n" + fromUser + "\r\n" + fileName + "\r\n" + fileSize;
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				break;
			}
		}
	}

	/**
	 * �ܾ������ļ�
	 * 
	 * @param toUser
	 *            ���û�
	 * @param isRec
	 *            �Ƿ����
	 */
	public void sendFileResponse(String toUser, String isRec)
	{
		String forword = "@SendFileResponse@\r\n"+isRec+"\r\n" + toUser;
		System.out.println("�����ظ��ļ�����" + forword);
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				return;
			}
		}
	}

	/**
	 * ͬ�����
	 * 
	 * @param toUser
	 *            ���û�
	 * @param isRec
	 *            �Ƿ����
	 * @param ip
	 *            ����ip
	 */
	public void sendFileResponse(String toUser, String isRec, String ip)
	{
		String forword = "@SendFileResponse@\r\n"+isRec+"\r\n" + toUser + "\r\n" + ip;
		System.out.println("�����ظ��ļ�����" + forword);
		for (NameSocket socket : sockets)
		{
			if (socket.getName().equals(toUser))
			{
				socket.sendMessage(forword);
				return;
			}
		}
	}

	public void sendOnLineList()
	{

		StringBuffer message = new StringBuffer("@OnLineList@\r\n" + serverNickName);

		// ��ȡ�����б�
		for (NameSocket socket : sockets)
		{
			message.append("&" + socket.getName());
		}

		System.out.println("�������͵��б�" + message.toString());
		// ��������
		for (NameSocket socket : sockets)
		{
			socket.sendMessage(message.toString());
		}

	}

	/**
	 * ����Ϣ��ʾ����Ļ
	 */
//	public static void toScreen(String nickName, String message)
//	{
//		ChatRoomUI.getInstance().getChatRecordTextArea().append(nickName + " �� " + message + "\n");
//		int height= 10999 ;   
//	    Point p = new  Point();   
//	    p.setLocation(0 , ChatRoomUI.getInstance().getChatRecordTextArea().getLineCount()*height);   
//	    ChatRoomUI.getInstance().getSp().getViewport().setViewPosition(p);
//	}  
	

	public LinkedList<NameSocket> getSockets()
	{
		return sockets;
	}

	public String getServerNickName()
	{
		return serverNickName;
	}

	public void setServerNickName(String serverNickName)
	{
		this.serverNickName = serverNickName;
	}

}
