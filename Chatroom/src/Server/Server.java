package Server;
import java.awt.EventQueue;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import java.awt.BorderLayout;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Server {

	private JFrame frame;
	private static ServerSocket ss;
	private static Socket s;
	private static DataInputStream din;
	private static DataOutputStream dout;
	private static JTextArea msgbox = new JTextArea();
	private JTextField inputField;
	
	static ArrayList<ClientHandler> clients = new ArrayList<ClientHandler>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) throws Exception {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Server window = new Server();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		String alias = "";
		String msgin = "";

		
		//make the socket
		ss = new ServerSocket(2001);
		//continuously listen on it
		while(true) {
			Socket s = null;
			
			try {
				s = ss.accept();
				
				DataInputStream dis = new DataInputStream(s.getInputStream());
				DataOutputStream dout = new DataOutputStream(s.getOutputStream());
				
				ClientHandler t = new ClientHandler(s, dis, dout);
				
				t.start();
				clients.add(t);
			}
			catch(Exception e) {
				System.out.println("oh god");
				s.close();
				System.out.println(e.toString());
			}
		}
		
		/*try {
			ss = new ServerSocket(2001);
			s = ss.accept();
			
			din = new DataInputStream(s.getInputStream());
			dout = new DataOutputStream(s.getOutputStream());
			
			while(!msgin.equals("exit")) {
				msgin = din.readUTF();
				msgbox.setText(msgbox.getText().trim() + "\n Client:" + msgin);
			}
		}catch(Exception e) {
			e.printStackTrace();
		}*/
	}

	/**
	 * Create the application.
	 */
	public Server() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 450, 300);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		msgbox.setBounds(10, 11, 414, 208);
		frame.getContentPane().add(msgbox);
		
		inputField = new JTextField();
		inputField.setBounds(10, 230, 315, 20);
		frame.getContentPane().add(inputField);
		inputField.setColumns(10);
		
		/*JButton msgSend = new JButton("SEND");
		msgSend.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				try {
					String msgout = "";
					msgout = inputField.getText().trim();
					dout.writeUTF(msgout);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		});
		msgSend.setBounds(335, 229, 89, 23);
		frame.getContentPane().add(msgSend);*/
	}
}

class ClientHandler extends Thread {
	
	final Socket s;
	final DataInputStream in;
	final DataOutputStream out;
	
	public ClientHandler(Socket s, DataInputStream in, DataOutputStream out) {
		this.s = s;
		this.in = in;
		this.out = out;
	}
	
	public void run() {
	String received = "";
	String sent;
	while(true) {
		try{
			received = in.readUTF();
			for(ClientHandler x:Server.clients) {
				x.out.writeUTF("\n"+received);
			}
		}
		catch(Exception e) {
			System.out.println("Handler issue");
		}
		
	}
	}

}

