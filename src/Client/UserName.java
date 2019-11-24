package Client;


import RemoteIF.IManageServer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class UserName implements ActionListener, Serializable {
    private JFrame frame;
    private JTextField txtUsername;
    private JButton btnNext;
    
    Registry registry;
    IManageServer remoteServerState;
    
    String userType;
    
    Client client;
    
    boolean nameFlag = false;
    
    public UserName(Registry registry, IManageServer remoteServerState, String userType){
    	this.remoteServerState = remoteServerState;
    	this.registry = registry;
    	this.userType = userType;
    	
        initialise();

        btnNext.addActionListener(this);
    }

    private void initialise(){
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        frame.getContentPane().add(panel, BorderLayout.CENTER);
        panel.setLayout(null);

        JLabel lblWelcome = new JLabel("Welcome.");
        lblWelcome.setBounds(0, 50, 450, 16);
        lblWelcome.setHorizontalAlignment(SwingConstants.CENTER);
        panel.add(lblWelcome);

        txtUsername = new JTextField();
        txtUsername.setBounds(0, 120, 450, 30);
        txtUsername.setHorizontalAlignment(SwingConstants.CENTER);
        txtUsername.setText("Please enter a username...");
        panel.add(txtUsername);
        txtUsername.setColumns(1);

        btnNext = new JButton("Next");
        btnNext.setBounds(75, 175, 300, 29);
        panel.add(btnNext);

        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == btnNext) {
    		try {
    			if(remoteServerState.checkUserName(txtUsername.getText(), userType) == false) {
    				if(userType.equals("Manager")) {
    					frame.setVisible(false);
    					client = new Client(registry, remoteServerState, userType, txtUsername.getText());
    					remoteServerState.addClient(txtUsername.getText(), userType, client, client.remoteChat, client.remoteServer, client.remoteArt);
    				}
    				else {
    					if(remoteServerState.askPermission(txtUsername.getText())) {
    						frame.setVisible(false);
    						client = new Client(registry, remoteServerState, userType, txtUsername.getText());
    						remoteServerState.addClient(txtUsername.getText(), userType, client, client.remoteChat, client.remoteServer, client.remoteArt);
    					}
    				}
    			}
    			else {
    				JOptionPane.showMessageDialog(frame, "Duplicate username");
    			}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			} catch (NotBoundException e1) {
				e1.printStackTrace();
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
    		
    	}
    }
}
