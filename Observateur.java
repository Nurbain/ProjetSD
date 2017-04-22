import java.rmi.RemoteException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class Observateur extends Client{
  static final long serialVersionUID = 42;
  Fin typeFin;
  protected boolean tourParTour;
  protected int nbJoueur;
  protected int nbJoueurFini=0;
  
  private File fichier;
  private FileWriter fw;

  public Observateur(String name,Fin typeFin,boolean tourParTour, String Nomfichier) throws RemoteException{
    super(name);
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

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,Ressources r,int nombre,String nameReceveur,Type typeReceveur){

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
	    	  String IRL = (c.GetisJoueurIRL())? "IRL" : "noIRL" ;
	    	  
	    	  System.out.println(pseudo+" "+IRL+ "\n");
	    	  EcritureLog(pseudo+" "+IRL);
	      }
	      
	      System.out.println("Producteurs :\n");
	      for(int j=0;j<ListProducteur.size();j++)
	      {
	    	  ClientInterface c = ListProducteur.get(j);
	    	  String pseudo = c.getName();
	    	  String ressource = c.GetRessources().getName();
	    	  
	    	  System.out.println(pseudo+" "+ressource+"\n");
	    	  EcritureLog(pseudo+" "+ressource);
	      }
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
    
    if(tourParTour){
      tourDeJeu();
    }
  }

  public void PartieFini(String name){
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
      
      
      try {
		fw.close();
      } 
      catch (IOException e) {e.printStackTrace();}
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

}
