import java.rmi.server.UnicastRemoteObject ;
import java.rmi.RemoteException ;
import java.lang.*;

public class Client
extends UnicastRemoteObject // Hérite de UnicastRemoteObject
implements ClientInterface, Runnable // implémente l’interface
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

	public void run() {
		System.out.println("Run de "+this.name);
		int i=0;
		while(i< 20){
			System.out.println("ping");
			i++;
			try{
				Thread.sleep(1);
			}catch(InterruptedException e){System.out.println("pb Thread");}
		}
	}
}
