package Client;

import RemoteIF.IManageServer;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;

public class CreateWhiteboard implements ActionListener {
    private JFrame frame;
    private JTextField txtName;
    private JButton btnLetsGo;
    
    Registry registry;
    IManageServer remoteServerState;
    
    public CreateWhiteboard(Registry registry, IManageServer remoteServerState){
    	this.registry = registry;
    	this.remoteServerState = remoteServerState;
    	
        initialize();
        frame.setVisible(true);

        btnLetsGo.addActionListener(this);

    }

    private void initialize(){
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        JLabel lblPleaseEnterA = new JLabel("Please enter a name for your Whiteboard");
        lblPleaseEnterA.setHorizontalAlignment(SwingConstants.CENTER);
        lblPleaseEnterA.setBounds(20, 70, 400, 16);
        frame.getContentPane().add(lblPleaseEnterA);

        txtName = new JTextField();
        txtName.setText("boardname");
        txtName.setBounds(25, 124, 400, 30);
        frame.getContentPane().add(txtName);
        txtName.setColumns(10);

        btnLetsGo = new JButton("Let's Go!");
        btnLetsGo.setBounds(160, 184, 120, 30);
        frame.getContentPane().add(btnLetsGo);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == btnLetsGo) {
    		try {
				remoteServerState.setHost(true);
				remoteServerState.setBoardName(txtName.getText());
				
		        UserName username = new UserName(registry, remoteServerState, "Manager");
		        frame.setVisible(false);
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
    		
	        
    	}
    }
}