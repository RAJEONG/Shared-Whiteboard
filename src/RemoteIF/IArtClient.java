package RemoteIF;

import java.rmi.Remote;
import java.rmi.RemoteException;
import Client.Shape;

public interface IArtClient extends Remote {
	void setNewBoard() throws RemoteException;
	void drawSharedShape(Shape shape) throws RemoteException;
	void drawCurrentBoard(int[][] pixels) throws RemoteException;
	int[][] getManagerBoard() throws RemoteException;
}