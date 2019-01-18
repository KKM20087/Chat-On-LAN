package privatechat.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.filechooser.FileFilter;

import javax.swing.JFileChooser;


import domain.ClientThread;
import domain.ServerThread;
import privatechat.ui.PrivateChatUI;

public class PrivateChatSendFileListener implements ActionListener
{

	public static File file;
	private String title;// �Է�id
	private PrivateChatUI privateChat;

	public PrivateChatSendFileListener(String title, PrivateChatUI privateChat)
	{
		this.title = title;
		this.privateChat = privateChat;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("�����ļ�");
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileFilter()
		{
			@Override
			public String getDescription()
			{
				// ��ʾ�������͵�
				return ".*";
			}

			@Override
			public boolean accept(File f)
			{
				return true;
			}
		});
		int returnVal = fc.showOpenDialog(privateChat);	//���ص�״̬
		file = fc.getSelectedFile();
		
		
		if (returnVal == JFileChooser.APPROVE_OPTION)
		{
			if (ServerThread.getIntance().getServerNickName() == null) // ������ǳ�Ϊ��
			{
				System.out.println("���Ӷ˷����ļ�����");
				ClientThread.getInstance().send(
						"@FileToOne@\r\n" + title + "\r\n" + ClientThread.getInstance().getClientName() + "\r\n",
						file.getName() + "\r\n" + file.length());
				System.out.println("FileUtils.sendFile ��Ҫ���͵��ļ�"+PrivateChatSendFileListener.file);
			} else
			{
				System.out.println("����˷����ļ�����");
				// ServerThread.getIntance().sendToOne(title,ServerThread.getIntance().getServerNickName(),
				// message);
				ServerThread.getIntance().sendFileMessage(title, ServerThread.getIntance().getServerNickName(),
						file.getName(), new Double(file.length()).toString());
				System.out.println("FileUtils.sendFile ��Ҫ���͵��ļ�"+PrivateChatSendFileListener.file);
			}
		}

	}

}
