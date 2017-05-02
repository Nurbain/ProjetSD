import java.net.* ;
import java.rmi.* ;

/**@author WENDLING Quentin URBAIN Nathan*/

public class ProducteurServ
{
	public static void main(String [] args)
	{
		//Verifie que toute les donnees sont rentre
		if (args.length != 8)
		{
			System.out.println("Usage : java ProducteurServ <Nom Serveur> <port du serveur de noms> <Nom Producteur> <Nom ressources> <nbr de ressource initiale> <ratio de prod> <Nb de ressources pouvant etre donnee> <y si tour par tour n sinon>") ;
			System.exit(0) ;
		}
		try
		{
			//Cree le producteur
  			Producteur objLocal = new Producteur(args[2],"localhost",args[1],args[2],args[3],Integer.parseInt(args[4]),Float.valueOf(args[5]),Integer.parseInt(args[6]),(args[7].equals("y"))?true:false) ;
			Naming.rebind( "rmi://"+args[0]+":" + args[1] + "/" + args[2] ,objLocal);
			System.out.println("Serveur pret");
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
