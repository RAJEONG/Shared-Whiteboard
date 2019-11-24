package Server;

import RemoteIF.IChatClient;
import RemoteIF.IChatServer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class RemoteChat extends UnicastRemoteObject implements IChatServer, Serializable {
	private Server server;
	IChatClient user;

    public RemoteChat(Server server) throws RemoteException{
        this.server = server;
    }
    
    public void broadcastMessage(String message) throws RemoteException {
    	for(int i=0; i<server.clientList.size(); i++) {
    		user = (IChatClient) server.clientList.get(server.clientName.get(i));
    		user.printMessage(message);
    	}
    }
}
