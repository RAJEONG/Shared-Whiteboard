package RemoteIF;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Vector;

public interface IManageClient extends Remote {
	boolean askPermission(String userName) throws RemoteException;
	void updateClientList(Vector<String> clientName) throws RemoteException;
	void shutDown(String action) throws RemoteException, NotBoundException;
}