import java.net.* ;
import java.rmi.* ;
import java.lang.*;

public class JoueurServ
{
	public static void main(String [] args)
	{
		if (args.length != 2)
		{
			System.out.println("Usage : java JoueurServ <port du serveur de noms> <Nom Joueur>") ;
			System.exit(0) ;
		}
		try
		{
			Joueur objLocal = new Joueur(args[1],Personnalite.Individuel,false) ;
			//Thread t=new Thread(objLocal);
			//t.start();
			Naming.rebind( "rmi://localhost:" + args[0] + "/" + args[1] ,objLocal);
			System.out.println("Serveur pret");
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
