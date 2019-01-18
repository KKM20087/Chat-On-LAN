package utils;



import javax.swing.JOptionPane;

import domain.ClientThread;
import domain.NameSocket;
import selectroom.ui.SelectRoomUI;

public class InputNickNameCheck
{

	/**
	 * ������ȷ���ǳ��Ƿ��ظ�
	 * 
	 * @return �ǳ�
	 */
	public static String nickCheck(NameSocket socket)
	{
		try
		{
			String nickName;
			while (true)
			{
				nickName = JOptionPane.showInputDialog(SelectRoomUI.getInstance(), "�������ǳ�", "��ʾ", 1);
				
				if(nickName==null)
					System.exit(0);
				else if(nickName.equals(""))
					continue;
				System.out.println("�ǳƣ�" + nickName);
				
				socket.sendMessage("@NickName@\r\n" + nickName);
				byte[] buf = new byte[1024];
				socket.getSocket().getInputStream().read(buf);
				// �����˷������ݣ�����˷���1��ͨ��
				if(new String(buf).trim().equals("1"))
				{
					System.out.println("ͨ��");
					break;
				}
				else
				{
					System.out.println("���ǳ��Ѵ���");
					JOptionPane.showMessageDialog(SelectRoomUI.getInstance(), "���ǳ��Ѵ��ڣ�����������", "��ʾ", 0);
				}
			}
			ClientThread.getInstance().setClientName(nickName);
		} catch (Exception e)
		{
			// TODO: handle exception
		}

		return "";
	}
}
