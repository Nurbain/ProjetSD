import java.net.* ;
import java.rmi.* ;

/**@author WENDLING Quentin URBAIN Nathan*/

public class ObservateurServ
{
	public static void main(String [] args)
	{
		//Verifie que toute les donnees ont ete donnees 
		if (args.length != 4)
		{
			System.out.println("Usage : java ObservateurServ <port du serveur de noms> <Nom Observateur> <y si tour par tour n sinon> <Nom du fichier Log>") ;
			System.exit(0) ;
		}
		try
		{
			//Cree l'observateur
			Observateur objLocal = new Observateur (args[1],"localhost",args[0],args[1],Fin.Brute,(args[2].equals("y"))?true:false , args[3]) ;
			Naming.rebind( "rmi://localhost:" + args[0] + "/" + args[1] ,objLocal);
			System.out.println("Serveur pret") ;
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
