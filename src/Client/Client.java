package Client;

import RemoteIF.IArtClient;
import RemoteIF.IArtServer;
import RemoteIF.IChatClient;
import RemoteIF.IChatServer;
import RemoteIF.IManageClient;
import RemoteIF.IManageServer;

import java.awt.HeadlessException;
import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Vector;

public class Client extends UnicastRemoteObject implements IChatClient, IArtClient, IManageClient, Serializable {
    String userName;
    
    Registry registry;
    IChatServer remoteChat;
    IArtServer remoteArt;
    IManageServer remoteServer;
    
    String userType;
    
    ClientGUI gui;
    UserName userNameObj;
    
    String message;

    public Client(Registry registry, IManageServer remoteServer, String userType, String userName) throws NotBoundException, HeadlessException, IOException {
    	this.registry = registry;
    	this.userType = userType;
    	this.userName = userName;
    	
    	remoteChat = (IChatServer) this.registry.lookup("whiteboardChatSend");
        remoteArt = (IArtServer) this.registry.lookup("whiteboardArtSend");
        this.remoteServer = remoteServer;
        
        gui = new ClientGUI(this);
    }
    
    
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// Management Methods
    
    public boolean askPermission(String userName) throws RemoteException{
    	return gui.permission(userName);
    }
    
    public void disconnect(String userName, String userToKick) throws RemoteException, NotBoundException {
    	if(userToKick == null) {
    		remoteServer.disconnect(userName, null);
    		System.exit(0);
    	}
    	else {
    		remoteServer.disconnect(userName, userToKick);
    	}
    }
    
    public void updateClientList(Vector<String> clientName){
    	gui.updateClientList(clientName);
    }

    public void shutDown(String action) throws RemoteException {
    	System.out.println("Shut down");
    	gui.shutDown(action);
    	System.exit(0);
    }
    
    
    /////////////////////////////////////////////////////////////////////////////////////////////////////
    // Chat Methods
    
    public void sendMessage(String message) throws RemoteException {
    	this.remoteChat.broadcastMessage(message);
	}
	
	public void printMessage(String message){
		this.message = message + "\n";
		gui.chatArea.append(this.message);
		
	}
	
	
	/////////////////////////////////////////////////////////////////////////////////////////////////////
	// Art Methods
	
	public void newBoard() throws RemoteException {
		remoteArt.askNewBoard();
	}
	
	public void setNewBoard() {
		gui.graphics.clearRect(0, 0, gui.drawPanel.getWidth(), gui.drawPanel.getHeight());
		gui.backgroundGraphics.clearRect(0, 0, gui.drawPanel.getWidth(), gui.drawPanel.getHeight());
	}
	
	public void shareShape(Shape shape) throws RemoteException {
		remoteArt.broadcastShape(shape);
	}
	
	public void drawSharedShape(Shape shape) {
		gui.drawSharedShape(shape);
	}
	
	public void sendOpenedImage(int[][] pixels) throws RemoteException {
		remoteArt.broadcastOpenedImage(pixels);
	}
	
	public int[][] getManagerBoard() {
		return gui.getCurrentBoardImage(gui.bi);
	}
	
	public void drawCurrentBoard(int[][] pixels) {
		try {
			gui.drawCurrentBoard(pixels);
			gui.graphics.drawImage(gui.bi, 0, 0, null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
