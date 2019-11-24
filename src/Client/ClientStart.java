package Client;

import RemoteIF.IArtServer;
import RemoteIF.IChatServer;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;


public class ClientStart implements ActionListener {
	IArtServer remoteArt;
	IChatServer remoteChat, chat;
	
    private JPanel contentPane;
    private JTextField txtPleaseEnterIp;
    private JTextField txtPleaseEnterPort;
    private JButton btnNewButton;
    private JFrame frame;

    private String ip, port;
    
    public ClientStart(){
        initialize();

        btnNewButton.addActionListener(this);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setBounds(100, 100, 450, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        frame.setContentPane(contentPane);
        contentPane.setLayout(null);

        txtPleaseEnterIp = new JTextField();
        txtPleaseEnterIp.setBounds(5, 100, 440, 30);
        txtPleaseEnterIp.setText("localhost");
        contentPane.add(txtPleaseEnterIp);
        txtPleaseEnterIp.setColumns(10);

        txtPleaseEnterPort = new JTextField();
        txtPleaseEnterPort.setBounds(5, 150, 440, 30);
        txtPleaseEnterPort.setText("3000");
        contentPane.add(txtPleaseEnterPort);
        txtPleaseEnterPort.setColumns(10);

        btnNewButton = new JButton("Next");
        btnNewButton.setBounds(120, 210, 200, 29);
        contentPane.add(btnNewButton);

        JLabel lblConnectToServer = new JLabel("Connect to Server");
        lblConnectToServer.setHorizontalAlignment(SwingConstants.CENTER);
        lblConnectToServer.setBounds(160, 50, 116, 16);
        contentPane.add(lblConnectToServer);

        frame.setVisible(true);
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if(e.getSource() == btnNewButton) {
	        ip = txtPleaseEnterIp.getText();
	        port = txtPleaseEnterPort.getText();
	
	        try{
	            Registry registry = LocateRegistry.getRegistry(ip, Integer.parseInt(port));
	            HostOrJoin hostJoin = new HostOrJoin(registry);
	        }
	        catch(Exception f){
	            JOptionPane.showMessageDialog(frame, "No active server available at that port and IP combination.");
	            System.exit(0);
	        }
	        
	        frame.setVisible(false);
    	}
    }
    
    public static void main(String args[]) {
    	ClientStart clientStart = new ClientStart();
    }
}
