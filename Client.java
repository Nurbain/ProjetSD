import java.rmi.server.UnicastRemoteObject ;
import java.rmi.RemoteException ;
import java.lang.*;
import java.rmi.* ; 
import java.net.MalformedURLException ; 
import java.util.*;

public class Client
extends UnicastRemoteObject // Hérite de UnicastRemoteObject
implements ClientInterface, Runnable // implémente l’interface
{
	protected String name;
	protected ArrayList<ClientInterface> Peers= new ArrayList<ClientInterface>();

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

	public void run() {
		System.out.println("Run de "+this.name);
		int i=0;
		/*while(i< 20){
			System.out.println("ping");
			i++;
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){System.out.println("pb Thread");}
		}*/
	}
	
	
	public void ConnexionPeer(String ServerName,String PortNum,String namePeer) throws RemoteException {
		try
		{
		  System.out.println("Connexion a : "+namePeer);
		  ClientInterface b = (ClientInterface) Naming.lookup( "rmi://"+ServerName+":"+PortNum+"/"+namePeer ) ;
		  Peers.add(b); 
		}
		catch (NotBoundException re) { System.out.println(re) ; }
		catch (RemoteException re) { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ; }
	}
}
