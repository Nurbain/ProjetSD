import java.net.* ;
import java.rmi.* ;

/**@author WENDLING Quentin URBAIN Nathan*/


//Classe de creation du serveur observateur
public class ObservateurServ
{
	public static void main(String [] args)
	{
		//Verifie que toute les donnees ont ete donnees
		if (args.length != 6)
		{
			System.out.println("Usage : java ObservateurServ <Nom serveur> <port du serveur de noms> <Nom Observateur> <y si tour par tour n sinon> <Nom du fichier Log> <Type de Fin>") ;
			System.exit(0) ;
		}
		try
		{
			Fin typeFin=Fin.Brute;
			if(args[5].equals("Attente"))
				typeFin=Fin.Attente;
			//Cree l'observateur
			Observateur objLocal = new Observateur (args[2],"localhost",args[1],args[2],typeFin,(args[3].equals("y"))?true:false , args[4]) ;
			Naming.rebind( "rmi://"+args[0]+":" + args[1] + "/" + args[2] ,objLocal);
			System.out.println("Serveur pret") ;
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
