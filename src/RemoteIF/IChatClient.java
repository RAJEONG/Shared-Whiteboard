package RemoteIF;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatClient extends Remote {
	public void printMessage(String message) throws RemoteException;
	public void shutDown(String action) throws RemoteException;
}