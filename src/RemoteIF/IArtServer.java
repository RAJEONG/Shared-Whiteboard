package RemoteIF;

import java.rmi.Remote;
import java.rmi.RemoteException;

import Client.Shape;

public interface IArtServer extends Remote {
	void askNewBoard() throws RemoteException;
	void broadcastOpenedImage(int[][] pixels) throws RemoteException;
    void broadcastShape(Shape shape) throws RemoteException;
    void broadcastBoard() throws RemoteException;   
    int[][] managerBoardData() throws RemoteException;
}
