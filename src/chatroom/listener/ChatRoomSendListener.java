package chatroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import domain.ClientThread;
import domain.ServerThread;

public class ChatRoomSendListener implements ActionListener
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("������Ϣ");
		String message = ChatRoomUI.getInstance().getInputMessageTextArea().getText().replaceAll("\r\n", "\n");

		if (message == null || message.equals(""))
		{
			JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "���벻��Ϊ��", "����", 0);
			return;
		}
		if (ServerThread.getIntance().getServerNickName() == null) // ������ǳ�Ϊ��
		{
			System.out.println("���Ӷ˷�����Ϣ");
			ClientThread.getInstance().send("@MessageToAll@\r\n" + ClientThread.getInstance().getClientName() + "\r\n",
					message);
		} else
		{
			System.out.println("����˷�����Ϣ");
			ServerThread.getIntance().sendToAll(ServerThread.getIntance().getServerNickName(), message);
		}
		ChatRoomUI.getInstance().getInputMessageTextArea().setText("");

	}

}
