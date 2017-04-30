import java.rmi.RemoteException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;


public class Observateur extends Client{
  static final long serialVersionUID = 42;
  Fin typeFin;
  protected boolean tourParTour;
  protected int nbJoueur;
  protected int nbJoueurFini=0;

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

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event){
    generationLog(nameEmetteur,typeEmetteur,event,null,0,"",Type.Joueur);
  }

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,Ressources r,int nombre){
    generationLog(nameEmetteur,typeEmetteur,event,r,nombre,"",Type.Joueur);
  }

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,String nameReceveur,Type typeReceveur){
	    generationLog(nameEmetteur,typeEmetteur,event,null,0,nameReceveur,typeReceveur);
	  }

  public synchronized void generationLog(String nameEmetteur,Type typeEmetteur,Action event,Ressources r,int nombre,String nameReceveur,Type typeReceveur){

	  //On fonction de l'event le log change
	  switch(event)
	  {
	  	case Demande :
	  		String str1 = typeEmetteur+"  "+nameEmetteur+" Prend "+r.getName()+"  "+nombre+"  "+typeReceveur+"  "+nameReceveur;
	  		System.out.println(str1);
	  		EcritureLog(str1);
	  		break;

	  	case Production :
	  		String str2 = typeEmetteur+"  "+nameEmetteur+"  Produit  "+r.getName()+"  "+nombre;
	  		System.out.println(str2);
	  		EcritureLog(str2);
	  		break;

	  	case Vol:
	  		String str3 = typeEmetteur+"  "+nameEmetteur+"  Vol  "+r.getName()+"  "+nombre+"  "+typeReceveur+"  "+nameReceveur;
	  		System.out.println(str3);
	  		EcritureLog(str3);
	  		break;

	  	case Punition:
	  		String str4 = typeEmetteur+"  "+nameEmetteur+"  Punit "+typeReceveur+"  "+nameReceveur ;
	  		System.out.println(str4);
	  		EcritureLog(str4);
	  		break;

	  	case Fin:
	  		String str5 = typeEmetteur+"  "+nameEmetteur+" fini";
	  		System.out.println(str5);
	  		EcritureLog(str5);
	  		break;

	  	default:
	  		break;
	  }
  }

  public void LogDebutJeu()
  {
	  String mode = (this.tourParTour)? "TPT" : "Auto" ;
	  System.out.println("Mode "+mode+ "\n");
	  EcritureLog("Mode "+mode);
	  try{
		  System.out.println("Objectif "+ListJoueur.get(0).Getobjectif()+"\n");
		  EcritureLog("Objectif "+ListJoueur.get(0).Getobjectif());

		  System.out.println("Joueurs : \n");
		  EcritureLog("Joueurs :");
	      for(int i=0;i < ListJoueur.size();i++){
	    	  ClientInterface c = ListJoueur.get(i);
	    	  String pseudo = c.getName();

	    	  System.out.println(pseudo+"\n");
	    	  EcritureLog(pseudo);
	      }

	      System.out.println("Producteurs :\n");
	      EcritureLog("\nProducteurs :");
	      for(int j=0;j<ListProducteur.size();j++)
	      {
	    	  ClientInterface c = ListProducteur.get(j);
	    	  String pseudo = c.getName();
	    	  String ressource = c.GetRessources().getName();
	    	  int nombre = c.GetRessources().getExemplaires();
	    	  System.out.println(pseudo+" "+ressource+" "+nombre+"\n");
	    	  EcritureLog(pseudo+" "+ressource+" "+nombre);
	      }
	      EcritureLog("");
	  }
	  catch(RemoteException re) { System.out.println(re) ; }
  }

  public void EcritureLog(String log)
  {
	try {
		fw.write(log);
		fw.write("\n");
	}
	catch (IOException e) {e.printStackTrace();}

  }


  public void startAgent(){
    System.out.println(name + " Debut partie");
    LogDebutJeu();
    try{

      for(int i=0;i < ListJoueur.size();i++){
        ListJoueur.get(i).startAgent();
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

  public synchronized void PartieFini(String name){
    generationLog(name , Type.Joueur , Action.Fin);
    nbJoueurFini++;
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

      creationLog();
      long fin = System.currentTimeMillis() - StartTimer;
      try {
    	fw.write(String.valueOf(fin));
		  fw.close();
      }
      catch (IOException e) {e.printStackTrace();}
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

  public void tourDeJeu(){
    while(true && !finParti){
      try{
        for(int i=0;i < ListJoueur.size();i++){
          ListJoueur.get(i).tourDeJeu();
        }
        for(int i=0;i < ListProducteur.size();i++){
          ListProducteur.get(i).tourDeJeu();
        }
      }catch (RemoteException re) { System.out.println(re) ; }
    }
  }

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
