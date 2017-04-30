import java.net.* ;
import java.rmi.* ;

public class JoueurServ
{
	public static void main(String [] args)
	{
		if (args.length != 4)
		{
			System.out.println("Usage : java JoueurServ <port du serveur de noms> <Nom Joueur> <y si tour par tour n sinon> <y si joueur humain n sinon>") ;
			System.exit(0) ;
		}
		try
		{
			Joueur objLocal = new Joueur(args[1],"localhost",args[0],args[1],Personnalite.Individuel,(args[3].equals("y"))?true:false,100,(args[2].equals("y"))?true:false) ;
			//Thread t=new Thread(objLocal);
			//t.start();
			Naming.rebind( "rmi://localhost:" + args[0] + "/" + args[1] ,objLocal);
			System.out.println("Serveur pret");
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
