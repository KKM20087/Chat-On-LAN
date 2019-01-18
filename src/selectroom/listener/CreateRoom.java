package selectroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import domain.ServerThread;
import selectroom.ui.SelectRoomUI;


public class CreateRoom implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("��������");
		SelectRoomUI.getInstance().dispose();
		String serverName;
		
		while(true)
		{
			serverName=JOptionPane.showInputDialog(SelectRoomUI.getInstance(), "�������ǳ�", "��ʾ", 1);
			if(serverName==null)
				System.exit(0);
			else if(serverName.equals(""))
				continue;
			else
				break;
		}
		
		System.out.println("������ǳ�"+serverName);
		ServerThread.getIntance().setNickName(serverName);
		ChatRoomUI.init(); //��ʼ��������
		ServerThread.getIntance().start();//������߳�����
	}

}
