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
	//Tout les autres clients avec les quels je suis connecté
	protected ArrayList<ClientInterface> Peers= new ArrayList<ClientInterface>();
	protected ArrayList<JoueurInterface> ListJoueur =new ArrayList<JoueurInterface>();
	protected ArrayList<ProducteurInterface> ListProducteur =new ArrayList<ProducteurInterface>();

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

	/*Ajoute un client au client avec les quels je suis connecté
		namePeer Nom du client
		Cherche sur rmi://<ServerName>:<NumPort>/<namePeer>*/
	public void ConnexionPeer(String ServerName,String PortNum,String namePeer) throws RemoteException {
		try
		{
		  System.out.println("Connexion a : "+namePeer);
		  //Me connecte au client passé en paramettre
		  ClientInterface b = (ClientInterface) Naming.lookup( "rmi://"+ServerName+":"+PortNum+"/"+namePeer ) ;
			if(b instanceof JoueurInterface){
				ListJoueur.add((JoueurInterface)b);
				return;
			}else if(b instanceof ProducteurInterface){
				ListProducteur.add((ProducteurInterface)b);
				return;
			}
		  //Puis l'ajoute à la liste
		  Peers.add(b);
		}
		catch (NotBoundException re) { System.out.println(re) ; }
		catch (RemoteException re) { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ; }
	}
}
