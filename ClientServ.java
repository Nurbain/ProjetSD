import java.net.* ;
import java.rmi.* ;

/**@author WENDLING Quentin URBAIN Nathan*/

public class ClientServ
{
	public static void main(String [] args)
	{

		//Verifie que le port de serveur et le nom du client est donnee
		if (args.length != 2)
		{
			System.out.println("Usage : java ClientServ <port du serveur de noms> <Nom Client>") ;
			System.exit(0) ;
		}
		try
		{
			//Cree le client suivant les infos rentre
			Client objLocal = new Client (args[1],"localhost",args[0],args[1]) ;
			Thread t=new Thread(objLocal);
			t.start();
			Naming.rebind( "rmi://localhost:" + args[0] + "/" + args[1] ,objLocal);
			System.out.println("Serveur pret") ;
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
