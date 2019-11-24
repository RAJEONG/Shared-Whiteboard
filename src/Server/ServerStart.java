package Server;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.rmi.AlreadyBoundException;
import java.rmi.RemoteException;

public class ServerStart implements ActionListener {
	private JPanel contentPane;
    private JTextField txtPleaseEnterIp;
    private JTextField txtPleaseEnterPort;
    private JButton btnNewButton;
    private JFrame frame;

    private String ip;
    private String port;
    
	public ServerStart(){
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

        JLabel lblConnectToServer = new JLabel("Start Server");
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
	        
	        try {
	        	frame.setVisible(false);
	            Server server = new Server(); // create server object
	
	            if (server.startServer(Integer.parseInt(port), ip)) {
	                System.out.println("The server is running.");
	            }
	            else {
	                JOptionPane.showMessageDialog(null, "System error", "System Message", JOptionPane.OK_OPTION);
	            }
	
	        } catch (NumberFormatException e1) {
	            e1.printStackTrace();
	        } catch (RemoteException e1) {
	            e1.printStackTrace();
	        } catch (AlreadyBoundException e1) {
	            e1.printStackTrace();
	        }
    	}
    }
	
    public static void main(String[] args) {
    	new ServerStart();
    }

}