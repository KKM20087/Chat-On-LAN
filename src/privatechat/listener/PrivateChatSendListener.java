package privatechat.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.JTextArea;


import domain.ClientThread;
import domain.ServerThread;
import privatechat.ui.PrivateChatUI;


public class PrivateChatSendListener implements ActionListener
{
	private String title;
	private JTextArea inputMessageTextArea;
	private JTextArea chatRecordTextArea;
	private PrivateChatUI privateChatUI;
	public PrivateChatSendListener(String title,JTextArea inputMessageTextArea,JTextArea chatRecordTextArea,PrivateChatUI privateChatUI)
	{
		
		this.privateChatUI=privateChatUI;
		this.title=privateChatUI.getTitleUser();
		this.inputMessageTextArea=privateChatUI.getInputMessageTextArea();
		this.chatRecordTextArea=privateChatUI.getChatRecordTextArea();
	}
	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("����˽����Ϣ");
		String message = inputMessageTextArea.getText().replaceAll("\r\n", "\n");
		
		if (message == null || message.equals(""))
		{
			JOptionPane.showMessageDialog(chatRecordTextArea, "���벻��Ϊ��", "����", 0);
			return;
		}
		if (ServerThread.getIntance().getServerNickName() == null) // ������ǳ�Ϊ��
		{
			System.out.println("���Ӷ˷���˽����Ϣ");
			ClientThread.getInstance().send("@MessageToOne@\r\n"+title+"\r\n"+ClientThread.getInstance().getClientName()+"\r\n",message);
			//chatRecordTextArea.append(ClientThread.getInstance().getClientName()+":"+message+"\r\n");
			privateChatUI.toScreen(ClientThread.getInstance().getClientName(), message);
			
		
		} else
		{
			//�����˽��û�н�����add
			System.out.println("����˷���˽����Ϣ");
			ServerThread.getIntance().sendToOne(title,ServerThread.getIntance().getServerNickName(), message);
			//chatRecordTextArea.append(ServerThread.getIntance().getServerNickName()+":"+message+"\r\n");
			privateChatUI.toScreen(ServerThread.getIntance().getServerNickName(), message);
		}
		privateChatUI.getInputMessageTextArea().setText("");
		
	}

}
