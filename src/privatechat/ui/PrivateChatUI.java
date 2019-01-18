package privatechat.ui;

import java.awt.Color;

import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.HashMap;


import javax.swing.BorderFactory;
import javax.swing.JButton;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import chatroom.ui.ChatRoomUI;
import privatechat.listener.PrivateChatExitListener;
import privatechat.listener.PrivateChatSendFileListener;
import privatechat.listener.PrivateChatSendListener;

public class PrivateChatUI extends JFrame
{
	private static final long serialVersionUID = 1L;
	private JTextArea chatRecordTextArea = new JTextArea(); // �����¼
	private JTextArea inputMessageTextArea = new JTextArea();// ��Ϣ�����
	private JScrollPane sp=null; //�����¼�������
	private JButton exitButton = new JButton("�ر�˽��");// �˳���ť
	private JButton sendButton = new JButton("����");// ���Ͱ�ť
	private JButton sendFileButton = new JButton("�����ļ�");// ���Ͱ�ť
	private String title; // ���ڱ���
	public static HashMap<String, PrivateChatUI> privateChatUIs = new HashMap<>();// ˽�Ĵ���

	public PrivateChatUI(String title)
	{
		this.title = title;
		setTitle("��  " + title + "  �ĶԻ�");
		setBounds(490, 150, 605, 650);
		setResizable(false);

		JPanel panel = new JPanel();
		setContentPane(panel);
		panel.setLayout(null);

		JPanel chatRecordPanel = new JPanel();
		chatRecordPanel.setLayout(new GridLayout(1, 1));
		chatRecordPanel.setBorder(BorderFactory.createLineBorder(Color.black));
		chatRecordPanel.setBounds(0, 0, 599, 400);
		sp= new JScrollPane(chatRecordTextArea);
		chatRecordTextArea.setMargin(new Insets(10, 10, 10, 10));//�ı����ı���ı߾�
		chatRecordTextArea.setEditable(false);//���ɱ༭
		chatRecordTextArea.setLineWrap(true);
		chatRecordTextArea.setFont(new Font("΢���ź�", 0, 16));	
		chatRecordTextArea.setFocusable(false);
		chatRecordPanel.add(sp);
		panel.add(chatRecordPanel);

		JPanel inputMessagePanel = new JPanel();
		inputMessagePanel.setLayout(new GridLayout(1, 1));
		inputMessagePanel.setBounds(0, 400, 600, 150);
		inputMessageTextArea.setLineWrap(true);
		inputMessageTextArea.setFont(new Font("΢���ź�", 0, 16));
		inputMessagePanel.add(inputMessageTextArea);
		panel.add(inputMessagePanel);

		JPanel functionPanel = new JPanel();
		functionPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 20));
		functionPanel.setBounds(0, 550, 600, 150);

		functionPanel.add(sendFileButton);
		functionPanel.add(exitButton);
		functionPanel.add(sendButton);

		sendFileButton.addActionListener(new PrivateChatSendFileListener(title, this));
		exitButton.addActionListener(new PrivateChatExitListener(title));
		sendButton.addActionListener(new PrivateChatSendListener(title, inputMessageTextArea, chatRecordTextArea,this));

		panel.add(functionPanel);

		final String titleTemp = title;
		addWindowListener(new WindowAdapter()
		{
			@Override
			public void windowClosing(WindowEvent e)
			{
				PrivateChatUI.removeChatUI(titleTemp);
				System.out.println("�ر���˽�Ĵ���");
			}
		});
		setVisible(true);
	}

	public void toScreen(String fromUser, String message)
	{
		chatRecordTextArea.append(fromUser + " �� " + message + "\r\n");
		int  height= 99999 ; //������   
	    Point p = new  Point();   
	    p.setLocation(0 , chatRecordTextArea.getLineCount()*height);   
	    this.getSp().getViewport().setViewPosition(p);
	}

	// �Ӵ����б����Ƴ�����ָ����title
	public static void removeChatUI(String title)
	{

		// Iterator<PrivateChatUI> iterator=privateChatUIs.iterator();
		// while(iterator.hasNext())
		// {
		// PrivateChatUI chat=iterator.next();
		// if(chat.getTitleUser().equals(title))
		// privateChatUIs.remove(chat);
		// }
		privateChatUIs.remove(title).dispose();;
	}

	public String getTitleUser()
	{
		return title;
	}

	public void setTitleUser(String title)
	{
		this.title = title;
	}

	// new �´��ڲ������յ���Ϣ��ʾ
	public static void newInstance(String user, String message)
	{
		// LinkedList<PrivateChatUI> chats = PrivateChatUI.privateChatUIs;
		// boolean flag = false; // ԭ�д����Ƿ���ڣ�Ĭ�ϲ�����
		// for (PrivateChatUI chat : chats)
		// {
		// if (chat.getTitleUser().equals(user))
		// {
		// chat.showMessage(user, message);
		// flag = true; // ����
		// }
		// }
		// if (!flag) // ��������new�´���
		// {
		// PrivateChatUI privateChatUI = new PrivateChatUI(user);
		// chats.add(privateChatUI);
		// privateChatUI.showMessage(user, message);
		// }
		HashMap<String, PrivateChatUI> uis = privateChatUIs;
		if (uis.containsKey(user))
		{ // ���ڴ˴���
			uis.get(user).toScreen(user, message);
		} else
		{
			// ������
			PrivateChatUI privateChatUI = new PrivateChatUI(user);
			uis.put(user, privateChatUI);
			privateChatUI.toScreen(user, message);
		}

	}

	// new �´���
	public static void newInstance(String user)
	{
//		LinkedList<PrivateChatUI> chats = PrivateChatUI.privateChatUIs;
//		boolean flag = false; // ԭ�д����Ƿ���ڣ�Ĭ�ϲ�����
//		for (PrivateChatUI chat : chats)
//		{
//			if (chat.getTitleUser().equals(user))
//			{
//				flag = true; // ����
//				JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "�����Ѵ���", "��ʾ", 1);
//			}
//		}
//		if (!flag) // ��������new�´���
//		{
//			PrivateChatUI privateChatUI = new PrivateChatUI(user);
//			chats.add(privateChatUI);
//		}
		HashMap<String, PrivateChatUI> uis = privateChatUIs;
		if (uis.containsKey(user))
		{ // ���ڴ˴���
			JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "�����Ѵ���", "��ʾ", 1);
		} else
		{
			// ������
			PrivateChatUI privateChatUI = new PrivateChatUI(user);
			uis.put(user, privateChatUI);
		}
		
	}

	public JScrollPane getSp()
	{
		return sp;
	}

	public void setSp(JScrollPane sp)
	{
		this.sp = sp;
	}

	public JTextArea getChatRecordTextArea()
	{
		return chatRecordTextArea;
	}

	public void setChatRecordTextArea(JTextArea chatRecordTextArea)
	{
		this.chatRecordTextArea = chatRecordTextArea;
	}

	public JTextArea getInputMessageTextArea()
	{
		return inputMessageTextArea;
	}

	public void setInputMessageTextArea(JTextArea inputMessageTextArea)
	{
		this.inputMessageTextArea = inputMessageTextArea;
	}
}
