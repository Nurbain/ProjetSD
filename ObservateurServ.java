import java.net.* ;
import java.rmi.* ;

public class ObservateurServ
{
	public static void main(String [] args)
	{
		if (args.length != 2)
		{
			System.out.println("Usage : java ObservateurServ <port du serveur de noms> <Nom Observateur>") ;
			System.exit(0) ;
		}
		try
		{
			Observateur objLocal = new Observateur (args[1],Fin.Brute,false) ;
			//Thread t=new Thread(objLocal);
			//t.start();
			Naming.rebind( "rmi://localhost:" + args[0] + "/" + args[1] ,objLocal);
			System.out.println("Serveur pret") ;
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
