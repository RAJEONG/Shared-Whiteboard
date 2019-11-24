package RemoteIF;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IChatServer extends Remote {
    void broadcastMessage(String message) throws RemoteException;
}
