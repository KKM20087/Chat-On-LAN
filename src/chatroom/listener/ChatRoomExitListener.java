package chatroom.listener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


public class ChatRoomExitListener implements ActionListener 
{

	@Override
	public void actionPerformed(ActionEvent e)
	{
		System.out.println("�˳�������");
		System.exit(0);
		
	}

}
