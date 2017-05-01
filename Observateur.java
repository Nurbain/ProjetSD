import java.rmi.RemoteException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

/**@author WENDLING Quentin URBAIN Nathan*/

//Classe de l'Agent Observateur 

public class Observateur extends Client{
  static final long serialVersionUID = 42;
  
	Fin typeFin;
  protected boolean tourParTour;
  
	//Nombre de joueur dans la partie
	protected int nbJoueur;
  
	//Nombre de Joueur ayant fini la partie
	protected int nbJoueurFini=0;

	//Fichier de log general
  private File fichier;
  private FileWriter fw;

  public Observateur(String name,String ServerName,String NumPort,String NomServise,Fin typeFin,boolean tourParTour, String Nomfichier) throws RemoteException{
    super(name,ServerName,NumPort,NomServise);
    this.typeFin=typeFin;
    this.monType=Type.Observateur;
    this.tourParTour=tourParTour;
    this.fichier = new File(Nomfichier);

    try {
		this.fw = new FileWriter(this.fichier);
	}
    catch (IOException e) {e.printStackTrace();}
  }

	//Generation du log de fin
  public void generationLog(String nameEmetteur,Type typeEmetteur){    
		String str = typeEmetteur+"  "+nameEmetteur+" fini";
	  System.out.println(str);
	  EcritureLog(str);
  }

	//Fonction d'ecriture de Log en debut de jeu
  public void LogDebutJeu()
  {
	  String mode = (this.tourParTour)? "TPT" : "Auto" ;
	  System.out.println("Mode "+mode+ "\n");
		//Ecriture du Mode	  
		EcritureLog("Mode "+mode);
	  try{
		  System.out.println("Objectif "+ListJoueur.get(0).Getobjectif()+"\n");
		  //Ecriture de l'objectif
			EcritureLog("Objectif "+ListJoueur.get(0).Getobjectif());

		  System.out.println("Joueurs : \n");
		  EcritureLog("Joueurs :");
			//Boucle ecrivant tout les noms des joueurs 
	      for(int i=0;i < ListJoueur.size();i++){
	    	  ClientInterface c = ListJoueur.get(i);
	    	  String pseudo = c.getName();

	    	  System.out.println(pseudo+"\n");
					//Ecriture dans le log du joueur
	    	  EcritureLog(pseudo);
	      }

	      System.out.println("Producteurs :\n");
	      EcritureLog("\nProducteurs :");
	      //Boucle ecrivant tout les noms des producteurs , leur ressource et leur nombre
				for(int j=0;j<ListProducteur.size();j++)
	      {
	    	  ClientInterface c = ListProducteur.get(j);
	    	  String pseudo = c.getName();
	    	  String ressource = c.GetRessources().getName();
	    	  int nombre = c.GetRessources().getExemplaires();
	    	  System.out.println(pseudo+" "+ressource+" "+nombre+"\n");
					//Ecriture dans le log du producteur ressource nombre
	    	  EcritureLog(pseudo+" "+ressource+" "+nombre);
	      }
	      EcritureLog("");
	  }
	  catch(RemoteException re) { System.out.println(re) ; }
  }

	//Fonction ecrivant dans le fichier de log le string donnee
  public void EcritureLog(String log)
  {
	try {
		fw.write(log);
		fw.write("\n");
	}
	catch (IOException e) {e.printStackTrace();}

  }

	//Starte l'agent observateur
  public void startAgent(){
    System.out.println(name + " Debut partie");
    LogDebutJeu();
    try{

      for(int i=0;i < ListJoueur.size();i++){
        ListJoueur.get(i).startAgent();
				//Compte le nombre de joueur dans la partie
        nbJoueur++;
      }
      for(int i=0;i < ListProducteur.size();i++){
        ListProducteur.get(i).startAgent();
      }
    }catch (RemoteException re) { System.out.println(re) ; }

    StartTimer = System.currentTimeMillis();

    if(tourParTour){
      tourDeJeu();
    }
  }

	//Fonction detectant la fin de partie
  public synchronized void PartieFini(String name){
		//Eciture dans le fichier le joueur gagnant
    generationLog(name , Type.Joueur);
    nbJoueurFini++;
    
		//Si la partie est en fin Brute alors dit au autre de s'arreter sinon attent que tous les joueurs aient fini
		if(this.typeFin == Fin.Brute || nbJoueur == nbJoueurFini){
      PartieFini();
      try{
        for(int i=0;i < ListJoueur.size();i++){
          ListJoueur.get(i).PartieFini();
        }
        for(int i=0;i < ListProducteur.size();i++){
          ListProducteur.get(i).PartieFini();
        }
      }catch (RemoteException re) { System.out.println(re) ; }

			//Cree les logs 
      creationLog();

			//Recupe la duree de la partie
      long fin = System.currentTimeMillis() - StartTimer;
      try {
			//Ecrit le temps de la partie
    	fw.write(String.valueOf(fin));
		  fw.close();
      }
      catch (IOException e) {e.printStackTrace();}
      
			//Deconnecte Tous les agents
			for(int i=0;i < ListJoueur.size();i++){
        try{
          ListJoueur.get(i).disconnect();
        }catch (RemoteException re) { continue; }
      }
      for(int i=0;i < ListProducteur.size();i++){
        try{
          ListProducteur.get(i).disconnect();
        }catch (RemoteException re) { continue ; }

      }
      try{
        this.disconnect();
      }catch (RemoteException re) { System.out.println(re) ; }

      return;

    }
  }

	//Fonction donnant le tour de jeu au different agents joueurs et producteur 
  public void tourDeJeu(){
    while(true && !finParti){
      try{
				//Tous les Joueurs jouent l'un apres l'autre
        for(int i=0;i < ListJoueur.size();i++){
          ListJoueur.get(i).tourDeJeu();
        }
				//Tous les producteurs jouent l'un apres l'autre
        for(int i=0;i < ListProducteur.size();i++){
          ListProducteur.get(i).tourDeJeu();
        }
      }catch (RemoteException re) { System.out.println(re) ; }
    }
  }

	//Fonction creeant les logs
  public void creationLog(){
    ArrayList<ArrayList<LogEntries>> listLog = new ArrayList<ArrayList<LogEntries>>();
    try{
      for(int i=0;i < ListJoueur.size();i++){
        listLog.add(ListJoueur.get(i).getLogPerso());
      }
      for(int i=0;i < ListProducteur.size();i++){
        listLog.add(ListProducteur.get(i).getLogPerso());
      }
    }catch (RemoteException re) { System.out.println(re) ; }

    LogEntries min = listLog.get(0).get(0);
    int tmp=0;
    while(!isEmpty(listLog)){
      for(int i=0;i<listLog.size();i++){
        System.out.println(listLog.get(i).size());
        if(listLog.get(i).size() == 0)
          continue;
        if(min.time > listLog.get(i).get(0).time){
          min=listLog.get(i).get(0);
          tmp=i;
          System.out.println("select : "+i);
        }
        System.out.println(listLog.get(i).get(0).time + "  "+listLog.get(i).get(0).action);
      }
      System.out.println("++++++++++++\n"+min.action+"\n++++++++++++");
      EcritureLog(min.action);
      System.out.println("tmp : "+tmp);
      listLog.get(tmp).remove(listLog.get(tmp).get(0));
      for(int i=0;i<listLog.size();i++){
        if(listLog.get(i).size() == 0)
          continue;
        min=listLog.get(i).get(0);
        tmp=i;
      }
    }

  }

  private boolean isEmpty(ArrayList<ArrayList<LogEntries>> l){
    for(int i=0;i<l.size();i++){
      if(!(l.get(i).size() == 0))
        return false;
    }
    System.out.println("Fini");
    return true;
  }

}
