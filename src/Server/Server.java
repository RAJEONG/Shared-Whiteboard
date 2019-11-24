package Server;

import RemoteIF.IArtServer;
import RemoteIF.IChatServer;
import RemoteIF.IManageServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.rmi.AlreadyBoundException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.HashMap;
import java.util.Vector;

public class Server implements Serializable, ActionListener {
	private IArtServer remoteArt;
    private IChatServer remoteChat;

    private IManageServer remoteServerState;
    private Registry registry;
    
    boolean hosting, serverClose;
    String boardName, managerName;
    
    Object manager;

    HashMap<String, Object> clientList; 
    HashMap<String, String> clientType;
    Vector<String> clientName;
    
    JFrame frame;
	JButton btnClose;
	JTextArea textArea;
	JScrollPane scrollPane;
	JLabel lblServerRun;
    
    public Server() {
    	clientList = new HashMap<String, Object>();
    	clientType = new HashMap<String, String>();
    	clientName = new Vector<String>();
    	
    	hosting = false;
    	serverClose = false;
    	
		initialize();
		
		btnClose.addActionListener(this);
	}
    
    private void initialize() {
    	frame = new JFrame();
    	frame.setBounds(100, 100, 676, 362);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		btnClose = new JButton("Close");
		btnClose.setBounds(260, 279, 152, 43);
		frame.getContentPane().add(btnClose);
		
		textArea = new JTextArea();
		textArea.setLineWrap(true);
		textArea.setEditable(false);
		
		scrollPane = new JScrollPane(textArea);
		scrollPane.setBounds(18, 42, 638, 225);
		frame.getContentPane().add(scrollPane);
		
		lblServerRun = new JLabel("Server : Run (0)");
		lblServerRun.setBounds(560, 8, 117, 22);
		frame.getContentPane().add(lblServerRun);
		

		JLabel lblConnectionHistory = new JLabel("Connection History");
		lblConnectionHistory.setBounds(18, 8, 186, 24);
		frame.getContentPane().add(lblConnectionHistory);
		
		frame.setVisible(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource() == btnClose) {
			try {
				serverClose = true;
				remoteServerState.disconnect(null, null);
			} catch (RemoteException | NotBoundException e1) {
				e1.printStackTrace();
			}
		}
		System.exit(0);
	}

    public IManageServer getRemoteServerState() {
        return remoteServerState;
    }

    public boolean startServer(int port, String ip) throws RemoteException, AlreadyBoundException{
    	
        LocateRegistry.createRegistry(port);
        registry = LocateRegistry.getRegistry(ip, port);

        if(registry == null) {
            return false;
        }
        else {
        	remoteArt = new RemoteArt(this);
        	remoteChat = new RemoteChat(this);
        	remoteServerState = new RemoteServerState(this);

        	registry.bind("whiteboardArtSend", remoteArt);
        	registry.bind("whiteboardChatSend", remoteChat);
        	registry.bind("hostingInfo", remoteServerState);
        	
            return true;
        }
    }
}
