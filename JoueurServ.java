import java.net.* ;
import java.rmi.* ;

public class JoueurServ
{
	public static void main(String [] args)
	{
		if (args.length != 6)
		{
			System.out.println("Usage : java JoueurServ <port du serveur de noms> <Nom Joueur> <y si tour par tour n sinon> <y si joueur humain n sinon> <Personnalite> <Objectif>") ;
			System.exit(0) ;
		}
		try
		{
			Personnalite p=Personnalite.Individuel;
			if(args[4].equals("Voleur")){
				p=Personnalite.Voleur;
			}else if(args[4].equals("Cooperatif")){
				p=Personnalite.Cooperatif;
			}else if (args[4].equals("Individuel")) {
				p=Personnalite.Individuel;
			}else if (args[4].equals("Stratege")) {
				p=Personnalite.Stratege;
			}else if (args[4].equals("Mefiant")) {
				p=Personnalite.Mefiant;
			}else if (args[4].equals("Rancunier")) {
				p=Personnalite.Rancunier;
			}
			Joueur objLocal = new Joueur(args[1],"localhost",args[0],args[1],p,(args[3].equals("y"))?true:false,Integer.parseInt(args[5]),(args[2].equals("y"))?true:false) ;
			//Thread t=new Thread(objLocal);
			//t.start();
			Naming.rebind( "rmi://localhost:" + args[0] + "/" + args[1] ,objLocal);
			System.out.println("Serveur pret");
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
