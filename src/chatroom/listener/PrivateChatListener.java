package chatroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import chatroom.ui.ChatRoomUI;

import domain.ServerThread;
import privatechat.ui.PrivateChatUI;

public class PrivateChatListener implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{

		if (ServerThread.getIntance().getServerNickName() == null) // ������ǳ�Ϊ��
		{
			System.out.println("���Ӷ˷���˽�Ĵ���");
		} else
		{
			System.out.println("����˷���˽�Ĵ���");
		}
		String nickName=ChatRoomUI.getInstance().getComboSelect();
		if(!(nickName==null))
			PrivateChatUI.newInstance(nickName);
	}

}
