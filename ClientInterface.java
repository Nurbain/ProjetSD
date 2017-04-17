import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface ClientInterface extends Remote // Hérite de Remote
{
	public String getName()
	throws RemoteException ;
	public void ConnexionPeer(String ServerName,String PortNum,String namePeer)
	throws RemoteException;
	public Type getmonType()
	throws RemoteException;
}
