package Client;

import RemoteIF.IArtServer;
import RemoteIF.IChatServer;
import RemoteIF.IManageServer;

import javax.swing.*;

import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AccessException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class HostOrJoin implements ActionListener {
    private JFrame frame;
    private JButton btnHost;
    private JButton btnJoin;
    IChatServer remoteChat;
    IArtServer remoteArt;
    IManageServer remoteServerState;
    Registry registry;
    
    
    public HostOrJoin(Registry registry) throws AccessException, RemoteException, NotBoundException {
    	this.registry = registry;
    	remoteServerState = (IManageServer) this.registry.lookup("hostingInfo");
    	
        initialize();
        
        btnHost.addActionListener(this);
        btnJoin.addActionListener(this);
    }

    
    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblWouldYouLike = new JLabel("Would you like to be a host or join an existing whiteboard?");
        lblWouldYouLike.setHorizontalAlignment(SwingConstants.CENTER);
        lblWouldYouLike.setBounds(20, 70, 400, 16);
        frame.getContentPane().add(lblWouldYouLike);

        btnHost = new JButton("Host");
        btnHost.setBounds(160, 119, 120, 29);
        frame.getContentPane().add(btnHost);

        btnJoin = new JButton("Join");
        btnJoin.setBounds(160, 155, 120, 29);
        frame.getContentPane().add(btnJoin);
        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == btnHost) {
    		try {
				if(!remoteServerState.hasHost()) {
					CreateWhiteboard window = new CreateWhiteboard(registry, remoteServerState);
					frame.setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(frame, "Someone is hosting the whiteboard\nPlease enter the whiteboard as a member");
				}
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
	    }
    	
    	else if(e.getSource() == btnJoin) {
    		try {
				if(remoteServerState.hasHost()) {
					UserName username = new UserName(registry, remoteServerState, "User");
					frame.setVisible(false);
				}
				else {
					JOptionPane.showMessageDialog(frame, "There are no available boards to join\nPlease host new whiteboard");
				}
			} catch (HeadlessException e1) {
				e1.printStackTrace();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
        }
    }
    
    
}
