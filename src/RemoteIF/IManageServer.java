package RemoteIF;

import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface IManageServer extends Remote {
	void setHost(Boolean hostFlag) throws RemoteException;	
	boolean hasHost() throws RemoteException;
	void setBoardName(String boardName) throws RemoteException;
	String getBoardName() throws RemoteException;
	boolean checkUserName(String userName, String type) throws RemoteException;
	boolean askPermission(String userName) throws RemoteException;
	void addClient(String userName, String userType, Object client, IChatServer remoteChat, IManageServer remoteServer, IArtServer remoteArt) throws RemoteException;
	void disconnect(String userName, String userToKick) throws RemoteException, NotBoundException;
	void updateClientList(String userName) throws RemoteException;
}