package selectroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import chatroom.ui.ChatRoomUI;
import domain.ClientThread;
import selectroom.ui.SelectRoomUI;
import utils.InputNickNameCheck;

public class EnterRoom implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		String ipGet=(String)SelectRoomUI.getInstance().getComboBox().getSelectedItem();
		if(ipGet.equals("������������"))
			return;
		System.out.println("���뷿��");
		SelectRoomUI.getInstance().dispose(); //ѡ�񷿼䴰�ڹر�
		ClientThread.init(ipGet);//���Ӷ��̳߳�ʼ��������ѡ���ip
		InputNickNameCheck.nickCheck(ClientThread.getInstance().getSocket());//������ȷ��id
		ChatRoomUI.init();//�����ҳ�ʼ��
		ClientThread.getInstance().start();//���Ӷ��߳�����
	}

}
