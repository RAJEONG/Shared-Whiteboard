package Server;

import RemoteIF.IArtClient;
import RemoteIF.IArtServer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import Client.Shape;


public class RemoteArt extends UnicastRemoteObject implements IArtServer, Serializable {
    private Server server;
    IArtClient manager;
    IArtClient iac;
    
    int[][] pixels;

    public RemoteArt(Server server) throws RemoteException {
        this.server = server;
    }
    
    public void askNewBoard() throws RemoteException {
    	for(int i=0; i<server.clientList.size(); i++) {
    		iac = (IArtClient) server.clientList.get(server.clientName.get(i));
    		iac.setNewBoard();
    	}
    }
    
    public void broadcastOpenedImage(int[][] pixels) throws RemoteException {
    	for(int i=0; i<server.clientList.size(); i++) {
    		iac = (IArtClient) server.clientList.get(server.clientName.get(i));
    		iac.drawCurrentBoard(pixels);
    	}
    }
    
    public void broadcastShape(Shape shape) throws RemoteException {
    	for(int i=0; i<server.clientList.size(); i++) {
    		iac = (IArtClient)server.clientList.get(server.clientName.get(i));
    		iac.drawSharedShape(shape);
    	}
    }
    
    public void broadcastBoard() throws RemoteException {
    	manager = (IArtClient) server.manager;
    	
    	for(int i=0; i<server.clientList.size(); i++) {
    		iac = (IArtClient)server.clientList.get(server.clientName.get(i));
    		iac.drawCurrentBoard(manager.getManagerBoard());
    	}
    }
    
    public int[][] managerBoardData() throws RemoteException {
    	manager = (IArtClient) server.manager;
		return manager.getManagerBoard();
    }
}
