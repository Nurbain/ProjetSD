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
    ClientInterface obs=new Client("O2");
	  //args[2] nom premier client args[taille-1] nom du dernier client
	  for(i=2;i<taille;i++){
		  System.out.println("rmi://"+args[0]+":"+args[1]+"/"+args[i]);
		  //Connection Client i
		  ClientInterface b = (ClientInterface) Naming.lookup( "rmi://"+args[0]+":"+args[1]+"/"+args[i] ) ;
      if(b.getmonType() == Type.Observateur){
        System.out.println("Wewe");
        obs=b;
      }
		  System.out.println("conn 1");
		  for(j=2;j<taille;j++){
			  if(j==i)
				continue;
				//Connecte le Client i au client j
        ClientInterface tmp = (ClientInterface) Naming.lookup( "rmi://"+args[0]+":"+args[1]+"/"+args[j] ) ;
			  b.ConnexionPeer(args[0],args[1],args[j],tmp.getmonType());
		  }
	  }
    obs.startAgent();
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (RemoteException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }
  }
}
