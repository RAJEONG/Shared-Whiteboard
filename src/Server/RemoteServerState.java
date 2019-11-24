package Server;

import RemoteIF.IManageServer;
import RemoteIF.IArtClient;
import RemoteIF.IArtServer;
import RemoteIF.IChatClient;
import RemoteIF.IChatServer;
import RemoteIF.IManageClient;

import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.time.LocalDate;
import java.time.LocalTime;


public class RemoteServerState extends UnicastRemoteObject implements IManageServer, Serializable {
	Server server;
	IChatServer rcs;
	IManageServer rms;
	IManageClient imc;
	IChatClient icc;
	IArtClient iac;
	IArtServer ras;

	
	public RemoteServerState(Server server) throws RemoteException {
		this.server = server;
	}
	
	public void setHost(Boolean hostFlag) {
		server.hosting = hostFlag;
	}
	
	public boolean hasHost(){
		return server.hosting ? true : false;
	}
	
	public void setBoardName(String boardName) {
		server.boardName = boardName;
	}

	public String getBoardName() {
		return server.boardName;
	}
	
	public boolean checkUserName(String userName, String type) {
		if(server.clientList.containsKey(userName)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public boolean askPermission(String userName) throws RemoteException {
		imc = (IManageClient) server.manager;
		
		if(imc.askPermission(userName)) {
			return true;
		}
		else {
			return false;
		}
	}
	
	public void addClient(String userName, String userType, Object client, IChatServer rcs, IManageServer rms, IArtServer ras) throws RemoteException {
		server.clientList.put(userName, client);
		server.clientType.put(userName, userType);
		server.clientName.add(userName);
		
		if(userType.equals("Manager")) {
			this.rcs = rcs;
			this.rms = rms;
			this.ras = ras;
			server.manager = (Object) client;
			server.managerName = userName;
		}
		
		updateClientList(userName);
		rcs.broadcastMessage("<Server> " + userName + " has logged in");
		server.textArea.append("[" + userType + "] '" + userName + "' is connected [" + LocalDate.now() + " " + LocalTime.now() + " ]\n\n");
		server.lblServerRun.setText("Server : Run (" + server.clientList.size() + ")");
	}
	
	public void disconnect(String userName, String userToKick) throws RemoteException, NotBoundException {
		IManageClient client;
		
		// Server is closed, close all connected clients
		if(server.serverClose == true) {
			for(Object user: server.clientList.values()) {
				try {
					client = (IManageClient) user;
					client.shutDown("ServerClose");
				} catch(RemoteException e) {}
			}
			server.clientList.clear();
			server.clientType.clear();
			server.clientName.clear();
		}

		
		else if(server.clientType.get(userName).equals("Manager")) {
			
			// Manager close the whiteboard and send shutdown message to all users
			if(userToKick == null) { 
				server.clientList.remove(userName);
				for(Object user: server.clientList.values()) {
					try {
						client = (IManageClient) user;
						client.shutDown("Quit");
					} catch(RemoteException e) {}
				}
				server.clientList.clear();
				server.clientType.clear();
				server.clientName.clear();
				setHost(false);
				server.textArea.append("[Manager] '" + userName + "' closes the whiteboard [" + LocalDate.now() + " " + LocalTime.now() + " ]\n\n");
				server.lblServerRun.setText("Server : Run (" + server.clientList.size() + ")");
			}
			
			// Manager kicks a user from the whiteboard
			else { 
				client = (IManageClient) server.clientList.get(userToKick);
				
				try {
					client.shutDown("Removed");
				} catch(RemoteException e) {}
				
				server.clientList.remove(userToKick);
				server.clientName.remove(userToKick);
				server.clientType.remove(userToKick);
				server.clientName.trimToSize();
				
				rcs.broadcastMessage("<Server> " + userToKick + " has logged out");
				updateClientList(userName);
				server.textArea.append("[User] '" + userToKick + "' is disconnected by manager [" + LocalDate.now() + " " + LocalTime.now() + " ]\n\n");
				server.lblServerRun.setText("Server : Run (" + server.clientList.size() + ")");
			}
		}
		
		else { // A user close the whiteboard
			server.clientList.remove(userName);
			server.clientName.remove(userName);
			server.clientType.remove(userName);
			server.clientName.trimToSize();
			
			rcs.broadcastMessage("<Server> " + userName + " has logged out");
			updateClientList(userName);
			server.textArea.append("[User] '" + userName + "' is disconnected [" + LocalDate.now() + " " + LocalTime.now() + " ]\n\n");
			server.lblServerRun.setText("Server : Run (" + server.clientList.size() + ")");
		}
	}
	
	public void updateClientList(String userName) throws RemoteException {
		// Update current active client List (when log-in / log-out / kicked from manager)
		for(int i=0; i<server.clientName.size(); i++) {
			imc = (IManageClient) server.clientList.get(server.clientName.get(i));
			imc.updateClientList(server.clientName);
		}
	}
}