import java.net.* ;
import java.rmi.* ;

/**@author WENDLING Quentin URBAIN Nathan*/

public class ProducteurServ
{
	public static void main(String [] args)
	{
		//Verifie que toute les donnees sont rentre
		if (args.length != 7)
		{
			System.out.println("Usage : java ProducteurServ <port du serveur de noms> <Nom Producteur> <Nom ressources> <nbr de ressource initiale> <ratio de prod> <Nb de ressources pouvant etre donnee> <y si tour par tour n sinon>") ;
			System.exit(0) ;
		}
		try
		{
			//Cree le producteur
  			Producteur objLocal = new Producteur(args[1],"localhost",args[0],args[1],args[2],Integer.parseInt(args[3]),Float.valueOf(args[4]),Integer.parseInt(args[5]),(args[6].equals("y"))?true:false) ;
			Naming.rebind( "rmi://localhost:" + args[0] + "/" + args[1] ,objLocal);
			System.out.println("Serveur pret");
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
