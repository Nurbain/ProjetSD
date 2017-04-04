import java.rmi.* ; 
import java.net.MalformedURLException ; 

public class Coordinateur
{
  public static void main(String [] args)
  {
    if (args.length < 2)
    {
      System.out.println("Usage : java Coordinateur <machine du Serveur> <port du rmiregistry>") ;
      System.exit(0) ;
    }
    try
    {
	  int i,j;
	  int taille = args.length;
	  for(i=2;i<taille;i++){
		  System.out.println("rmi://"+args[0]+":"+args[1]+"/C"+args[i]);
		  ClientInterface b = (ClientInterface) Naming.lookup( "rmi://"+args[0]+":"+args[1]+"/C"+args[i] ) ;
		  System.out.println("conn 1");
		  for(j=2;j<taille;j++){
			  if(j==i)
				continue;
			  b.ConnexionPeer(args[0],args[1],"C"+args[j]);
		  }
	  }
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
