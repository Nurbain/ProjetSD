import java.rmi.server.UnicastRemoteObject ;
import java.rmi.RemoteException ;

public class Client
extends UnicastRemoteObject // Hérite de UnicastRemoteObject
implements ClientInterface  // implémente l’interface
{
	protected String name;
	
	public Client (String name) throws RemoteException
	// Rmq : Le client n’a pas accès au constructeur
	{
		super() ;
		this.name=name;
	} ;
	public String getName() throws RemoteException
	{
		return this.name;
	}
}
