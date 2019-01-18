package utils;

import java.awt.Font;
import java.awt.GridLayout;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import chatroom.ui.ChatRoomUI;
import domain.ClientThread;
import domain.ServerThread;
import privatechat.listener.PrivateChatSendFileListener;

public class FileUtils
{
	private static long recFileSize; // �������ļ��Ĵ�С

	// ���ļ����ֽ�ת��ΪMb
	public static String sizetoMb(String fileSizeStr)
	{

		long fileSize = new Double(fileSizeStr).longValue();
		double size = (fileSize / (1024.0 * 1024));
		int temp = (int) (size * 1000);
		size = temp / 1000.0;
		return size + "";
	}

	/**
	 * 
	 * @param fromUser
	 *            ���û��ǳ�
	 * @param fileName
	 *            �ļ���
	 * @param fileSize
	 *            �ļ���С
	 */
	public static void confirmRecFile(String fromUser, String fileName, String fileSize)
	{
		int option = JOptionPane.showConfirmDialog(null,
				"�Ƿ� ��������<" + fromUser + ">���͵�<" + fileName + ">,��СΪ< " + sizetoMb(fileSize) + " Mb>");
		boolean recFile = false;// �Ƿ�����ļ�

		if (JOptionPane.OK_OPTION == option)
		{
			System.out.println("�����ļ�");
			recFile = true;
			FileUtils.recFileSize = new Double(fileSize).longValue();
		} else if (JOptionPane.NO_OPTION == option)
		{
			System.out.println("�ܾ������ļ�");
		} else if (JOptionPane.CANCEL_OPTION == option)
		{
			System.out.println("ȡ���Ի���");
		}

		if (recFile)// �����ļ�
		{
			if (ServerThread.getIntance().getServerNickName() == null) // ������ǳ�Ϊ��
			{// ���Ӷ�
				ClientThread.getInstance().send("@SendFileResponse@\r\n",
						"true\r\n" + fromUser + "\r\n" + IPUtils.getLocalIP() + "\r\n");
			} else
			{
				ServerThread.getIntance().sendFileResponse(fromUser, "true", IPUtils.getLocalIP());
			}
			FileUtils.recFile();
		} else // �ܾ�����
		{
			if (ServerThread.getIntance().getServerNickName() == null) // ������ǳ�Ϊ��
			{// ���Ӷ�
				ClientThread.getInstance().send("@SendFileResponse@\r\n", "false\r\n" + fromUser);
			} else
			{
				ServerThread.getIntance().sendFileResponse(fromUser, "false");
			}
		}

	}

	public static void sendFile(final String ip)
	{

		new Thread()
		{
			@Override
			public void run()
			{

				int length = 0;
				byte[] sendByte = null;
				Socket socket = null;
				DataOutputStream dout = null;
				FileInputStream fin = null;
				try
				{
					long count = 0;// �ѷ������ݵĴ�С
					JLabel label = showProgress(ChatRoomUI.getInstance());
					try
					{
						socket = new Socket();
						socket.connect(new InetSocketAddress(ip, 12346), 10 * 1000);// ָ����ʱ
						socket.setKeepAlive(true);
						System.out.println("���ӵ�Ŀ��ip");
						dout = new DataOutputStream(socket.getOutputStream());
						File file = PrivateChatSendFileListener.file; // ��ȡѡ����ļ�
						fin = new FileInputStream(file);
						sendByte = new byte[1024];
						dout.writeUTF(file.getName());
						while ((length = fin.read(sendByte, 0, sendByte.length)) > 0)
						{
							dout.write(sendByte, 0, length);
							dout.flush();
							count = count + length;
							label.setText("���ͽ���:" + count + "  /  " + file.length() + "  �ֽ�");
							System.out.println("������" + length);
						}
					} catch (Exception e)
					{
						// java Connection reset by peer: socket write error
						e.printStackTrace();
						JOptionPane.showMessageDialog(ChatRoomUI.getInstance(), "���ʹ���" + e.getMessage(), "����", 0);
					} finally
					{
						if (dout != null)
							dout.close();
						if (fin != null)
							fin.close();
						if (socket != null)
							socket.close();
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
				System.out.println("��������");
			}
		}.start();
	}

	public static void recFile()
	{

		new Thread()
		{
			public void run()
			{
				try
				{
					System.out.println("��ʼ����������");
					final ServerSocket server = new ServerSocket(12346);
					Socket socket = server.accept();

					long count = 0;// �ѽ������ݵĴ�С
					JLabel label = showProgress(ChatRoomUI.getInstance());
					byte[] inputByte = null;
					int length = 0;
					DataInputStream din = null;
					FileOutputStream fout = null;
					try
					{
						din = new DataInputStream(socket.getInputStream());
						String fileName = din.readUTF();
						System.out.println("test" + fileName);

						File file = new File("C:\\ChatRoom_RecFile\\");

						if (!file.exists())
							file.mkdir();

						file = new File("C:\\ChatRoom_RecFile\\" + fileName);
						int i = 1;
						while (file.exists())
						{
							file = new File("C:\\ChatRoom_RecFile\\" + "����" + i + fileName);
							i++;
						}
						fout = new FileOutputStream(file);
						inputByte = new byte[1024];
						System.out.println("��ʼ�����ļ�����...");
						while (true)
						{
							if (din != null)
							{
								length = din.read(inputByte, 0, inputByte.length);
							}
							if (length == -1)
							{
								break;
							}
							System.out.println(length);
							fout.write(inputByte, 0, length);
							fout.flush();
							count = count + length;
							label.setText("���ս���:" + count + "  /  " + FileUtils.recFileSize + "  �ֽ�");
						}
						System.out.println("��ɽ���");
						JOptionPane.showMessageDialog(null, "�ļ�������" + file.getAbsolutePath(), "�ļ��ѽ���", 1);

					} catch (Exception ex)
					{
						ex.printStackTrace();
						JOptionPane.showMessageDialog(null, "���մ���" + ex.getMessage(), "����", 1);
					} finally
					{
						if (fout != null)
							fout.close();
						if (din != null)
							din.close();
						if (socket != null)
							socket.close();
						server.close();
					}
				} catch (Exception e)
				{
					e.printStackTrace();
				}
			};
		}.start();
	}

	public static JLabel showProgress(JFrame frame)
	{
		JDialog dialog = new JDialog(frame, "����", false);

		JLabel label = new JLabel();
		label.setFont(new Font("΢���ź�", 0, 20));
		dialog.setSize(400, 100);
		dialog.setLocation(400, 400);
		dialog.setLayout(new GridLayout(1, 1));
		dialog.add(label);

		dialog.setVisible(true);
		return label;
	}

}
