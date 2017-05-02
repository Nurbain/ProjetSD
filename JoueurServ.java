import java.net.* ;
import java.rmi.* ;

/**@author WENDLING Quentin URBAIN Nathan*/

//Classe Lancent le Serveur Joueur 

public class JoueurServ
{
	public static void main(String [] args)
	{
		//Verifie que tout les arguments ont ete rentre
		if (args.length != 7)
		{
			System.out.println("Usage : java JoueurServ <adresse server> <port du serveur de noms> <Nom Joueur> <y si tour par tour n sinon> <y si joueur humain n sinon> <Personnalite> <Objectif>") ;
			System.exit(0) ;
		}
		try
		{
		//Set la personalite du joueur suivant la selection
			Personnalite p=Personnalite.Individuel;
			if(args[5].equals("Voleur")){
				p=Personnalite.Voleur;
			}else if(args[5].equals("Cooperatif")){
				p=Personnalite.Cooperatif;
			}else if (args[5].equals("Individuel")) {
				p=Personnalite.Individuel;
			}else if (args[5].equals("Stratege")) {
				p=Personnalite.Stratege;
			}else if (args[5].equals("Mefiant")) {
				p=Personnalite.Mefiant;
			}else if (args[5].equals("Rancunier")) {
				p=Personnalite.Rancunier;
			}
			
			//Cree le joueuer
			Joueur objLocal = new Joueur(args[2],args[0],args[1],args[2],p,(args[4].equals("y"))?true:false,Integer.parseInt(args[6]),(args[3].equals("y"))?true:false) ;
			Naming.rebind( "rmi://"+args[0]+":" + args[1] + "/" + args[2] ,objLocal);
			System.out.println("Serveur pret");
		}
		catch (RemoteException re)      { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ;  }
	}
}
