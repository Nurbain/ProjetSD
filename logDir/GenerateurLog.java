import java.util.ArrayList;
import java.io.*;

/**@author WENDLING Quentin URBAIN Nathan
*/

public class GenerateurLog{
  //Liste des historiques des joueurs
  private static ArrayList<JoueurLog> ListJoueur = new ArrayList<JoueurLog>();
  //Liste des historiques des producteurs
  private static ArrayList<ProducteurLog> ListProducteur = new ArrayList<ProducteurLog>();
  //Liste des historiques des ressources
  private static ArrayList<RessourcesLog> ListRessources = new ArrayList<RessourcesLog>();
  //Historique de l'état de chaque ressources aprés chaque action
  private static ArrayList<ArrayList<ArrayList<Integer>>> ListLog = new ArrayList<ArrayList<ArrayList<Integer>>>();
  //Historique de l'état de chaque joueurs aprés chaque action
  private static ArrayList<ArrayList<ArrayList<Integer>>> ListLogJoueur = new ArrayList<ArrayList<ArrayList<Integer>>>();
  //Mode de la partie Tour par tour / Temps réel
  private static String mode;
  //Objectif à atteindre pour chaque ressources
  private static int Objectif;

  public static void main(String[] args){
    //On verifie qu'on a bien les bon arguments
    if (args.length != 2)
		{
			System.out.println("Usage : java GenerateurLog <nom du fichier de log> <destination>") ;
			System.exit(0) ;
		}
    BufferedReader bis =null;
    FileWriter fw=null;

    try{
      //On initialise bis pour lire dans le fichier de log
      bis = new BufferedReader(new FileReader(new File(args[0])));
      String line;
      //On lis le mode de la partie
      LectureMode(bis);
      System.out.println("Mode = "+mode);
      //On lis l'objectif à atteindre
      LectureObjectif(bis);
      System.out.println("Objectif = "+Objectif);

      //On initialise les producteurs et joueurs
      while((line = bis.readLine()) != null){

        if(line.equals("Producteurs :")){
          //On initialise la liste de producteur
          LectureProducteur(bis);
        }

        if(line.equals("Classement :")){
          LectureClassement(bis);
        }

        if(line.equals("Fin de Partie :")){
          LectureFinPartie(bis);
          break;
        }

        if(line.equals("Joueurs :")){
          //On initialise la liste de joueur
          LectureJoueur(bis);
        }
      }

      //Pour chaque ressource on initialise son historique d'états et on initialise tout les joueur avec
      for(int i=0;i<ListRessources.size();i++){
        ListLog.add(new ArrayList<ArrayList<Integer>>());
        for(int j=0;j<ListJoueur.size();j++){
          ListJoueur.get(j).add(ListRessources.get(i).name,0);
        }
      }

      //On initialise l'Historique d'états de chaque joueurs
  	  for(int i=0;i<ListJoueur.size();i++){
  		  ListLogJoueur.add(new ArrayList<ArrayList<Integer>>());
  	  }

      //On lis toutes les actions
      while((line = bis.readLine()) != null){
        LectureAction(line);
      }
    }catch (FileNotFoundException e){e.printStackTrace();}
    catch (IOException e){e.printStackTrace();}
    finally{
      try{
        if(bis != null)
          bis.close();
      } catch (IOException e){ e.printStackTrace(); }
    }
    try{
      if(bis != null)
        bis.close();
    } catch (IOException e){ e.printStackTrace(); }

    //On écris le nombre de joueur dans un fichier temporaire
    try {
		    FileWriter fw2 = new FileWriter(new File("tmpParam"));
        fw2.write(""+ListJoueur.size());
        fw2.close();
	   }
    catch (IOException e) {e.printStackTrace();}

    //On écris le nombre de ressources dans un fichier temporaire
    try {
		    FileWriter fw3 = new FileWriter(new File("tmpParam2"));
        fw3.write(""+ListRessources.size());
        fw3.close();
	   }
    catch (IOException e) {e.printStackTrace();}

    try {
		    FileWriter fw3 = new FileWriter(new File("tmpParam3"));
        String tmp="";
        for(int i=0;i<ListRessources.size();i++){
          tmp=tmp+ListRessources.get(i).name+" ";
        }
        fw3.write(tmp);
        fw3.close();
	   }
    catch (IOException e) {e.printStackTrace();}

    try {
		    FileWriter fw3 = new FileWriter(new File("tmpParam4"));
        String tmp="";
        for(int i=0;i<ListJoueur.size();i++){
          tmp=tmp+ListJoueur.get(i).name+" ";
        }
        fw3.write(tmp);
        fw3.close();
	   }
    catch (IOException e) {e.printStackTrace();}

    try {
		    FileWriter fw3 = new FileWriter(new File("tmpParam5"));
        fw3.write(""+mode);
        fw3.write("\n"+Objectif);
        fw3.close();
	   }
    catch (IOException e) {e.printStackTrace();}

    //Creation des fichier GNUPlot pour les ressources
    for(int j=0;j<ListRessources.size();j++){
      try {
        fw = new FileWriter(new File(args[1]+j));
      }catch (IOException e) {e.printStackTrace();}

      ecrireGNUplot(fw,j);

      try {
        fw.close();
      }catch (IOException e) {e.printStackTrace();}
    }

    //Creation des fichier GNUPlot pour les joueur
    for(int j=0;j<ListJoueur.size();j++){
      try {
      fw = new FileWriter(new File(args[1]+"J"+j));
      }catch (IOException e) {e.printStackTrace();}

      ecrireGNUplotJoueur(fw,j);

      try {
      fw.close();
      }catch (IOException e) {e.printStackTrace();}
    }

    //Creation du fichier GNUplot pour l'avancement des joueur
    try {
        fw = new FileWriter(new File(args[1]+"GL"));
    }catch (IOException e) {e.printStackTrace();}

    ecrireGNUplotGlobal(fw);

    try {
      fw.close();
    }catch (IOException e) {e.printStackTrace();}
  }

  //Fonction pour lire la liste des Producteurs
  public static void LectureProducteur(BufferedReader bis){
    try{
      String line;
      while((line = bis.readLine()) != null){
        if(line.equals("")){
          return;
        }
        //log de la forme Nom NomRessource NbInitiale
        String delims = "[ ]+";
        //On separe les différentes info sur le producteur dans un tableau
        String[] tokens = line.split(delims);
        ListProducteur.add(new ProducteurLog(tokens[0],tokens[1],Integer.parseInt(tokens[2])));
        //Si la ressource n'est pas deja dans la liste des ressources on la rajoute
        if(!existRessources(tokens[1])){
          ListRessources.add(new RessourcesLog(tokens[1],0));
        }
      }
    }catch (IOException e){e.printStackTrace();}

  }

  //Fonction pour lire la liste des Joueurs
  public static void LectureJoueur(BufferedReader bis){
    try{
      String line;
      while((line = bis.readLine()) != null){
        if(line.equals("")){
          return;
        }
        ListJoueur.add(new JoueurLog(line));
      }
    }catch (IOException e){e.printStackTrace();}

  }

  public static void LectureFinPartie(BufferedReader bis){
    try{
      String line;
      line = bis.readLine();
      FileWriter fw = new FileWriter(new File("tmpTemps"));
      fw.write(line);
      fw.close();
    }catch (IOException e){e.printStackTrace();}

  }

  public static void LectureClassement(BufferedReader bis){
    String tmp="";
    try{
      String line;
      while((line = bis.readLine()) != null){
        if(line.equals("")){
          break;
        }
        tmp=tmp+line+" ";
      }
    }catch (IOException e){e.printStackTrace();}

    try {
        FileWriter fw = new FileWriter(new File("tmpClassement"));
        fw.write(tmp);
        fw.close();
     }
    catch (IOException e) {e.printStackTrace();}

  }

  //Fonction pour lire le mode de la partie
  public static void LectureMode(BufferedReader bis){
    try{
      String line;
      String delims = "[ ]+";
      if((line = bis.readLine()) != null){
        String[] tokens = line.split(delims);
        if(tokens.length != 2){
          System.out.println(line+" non reconu");
          return;
        }
        if(tokens[1].equals("Auto") || tokens[1].equals("TPT")){
          mode=tokens[1];
          return;
        }
        System.out.println(tokens[1]+" non reconu");
      }
    }catch (IOException e){e.printStackTrace();}

  }

  //Renvoie l'index de la ressource de nom "name" dans la liste des ressources
  public static int indexRessources(String name){
	  for(int i=0;i<ListRessources.size();i++){
		  if(ListRessources.get(i).name.equals(name))
			return i;
	  }
	  return -1;
  }

  //Renvoie l'index du Joueur de nom "name" dans la liste des joueurs
  public static int indexJoueur(String name){
	  for(int i=0;i<ListJoueur.size();i++){
		  if(ListJoueur.get(i).name.equals(name))
			return i;
	  }
	  return -1;
  }

  //Lecture de l'objectif de la partie
  public static void LectureObjectif(BufferedReader bis){
    try{
      String line;
      String delims = "[ ]+";
      if((line = bis.readLine()) != null){
        String[] tokens = line.split(delims);
        if(tokens.length != 2){
          System.out.println(line+" non reconu");
          return;
        }
        try{
          Objectif = Integer.parseInt(tokens[1]);
        }
        catch(NumberFormatException e){ System.out.println(tokens[1]+" non reconu"); }

      }
    }catch (IOException e){e.printStackTrace();}

  }

  //Renvoie le joueur de nom "name" null si il n'est pas dans la liste
  public static JoueurLog findJoueur(String name){
    for(int i=0;i<ListJoueur.size();i++){
      if(ListJoueur.get(i).name.equals(name))
        return ListJoueur.get(i);
    }
    return null;
  }

  public static ProducteurLog findProducteur(String name){
    for(int i=0;i<ListProducteur.size();i++){
      if(ListProducteur.get(i).name.equals(name))
        return ListProducteur.get(i);
    }
    return null;
  }

  //Lis une action dans le fichier de log
  public static void LectureAction(String line){
    String delims = "[ ]+";
    String[] tokens = line.split(delims);


    if(tokens[0].equals("Joueur")){
      //Si on lis une action sur un joueur
      JoueurLog jTmp=findJoueur(tokens[1]);

      if(tokens[2].equals("Prend")){
        //Si l'action est une prise de ressource
        //On rajoute dans le joueur concerné
        jTmp.add(tokens[3],Integer.parseInt(tokens[4]));
        //On rajoute les log pour la ressource et le joueur
        addLog(tokens[3]);
        addLogJoueur(tokens[1]);
        //On prend chez le producteur
        findProducteur(tokens[6]).sub(Integer.parseInt(tokens[4]));

      }
      else if(tokens[2].equals("Punit"))
      {
        //Si l'action est une punition
    	  JoueurLog JpTmp = findJoueur(tokens[6]);
        //On prends au joueur
    	  JpTmp.sub(tokens[3], Integer.parseInt(tokens[4]));
        jTmp.add(tokens[3],Integer.parseInt(tokens[4])/2);
        //On rajoute les log pour la ressource et le joueur
        addLog(tokens[3]);
    	  addLogJoueur(tokens[6]);
    	  addLogJoueur(tokens[1]);
      }
      else if(tokens[2].equals("Vol"))
      {
        //Si l'action est un vol
        //On rajoute au joueur qui vole
    	  jTmp.add(tokens[3],Integer.parseInt(tokens[4]));
        //On enleve au joueur qui se fais voler
    	  findJoueur(tokens[6]).sub(tokens[3],Integer.parseInt(tokens[4]));
        //On rajoute les log pour la ressource et le joueur
        addLog(tokens[3]);
        addLogJoueur(tokens[1]);
      }
    }else if(tokens[0].equals("Producteur")){
      //Si on lis une action sur un producteur
      ProducteurLog pTmp=findProducteur(tokens[1]);
      if(tokens[2].equals("Produit")){
        //Si il s'agit d'une production
        pTmp.add(Integer.parseInt(tokens[4]));
      }
    }

  }

  //Verifie si une ressource de nom "name" exist
  //true si elle existe false sinon
  public static boolean existRessources(String name){
    for(RessourcesLog r : ListRessources){
      if(r.name.equals(name))
        return true;
    }
    return false;
  }

  //Renvoie le nombre maximum d'action sur une ressource "name"
  public static int nbActionRessourcemax(String name){
    int tmp,max=0;
    for(int i=0;i<ListJoueur.size();i++){
      tmp = ListJoueur.get(i).nbActionRessource(name);
      if(tmp>max)
        max=tmp;
    }
    return max;
  }

  //Fonction qui écris le fichier GNUplot pour les ressources
  public static void ecrireGNUplot(FileWriter fw,int i){
    for(int j=0;j<ListLog.get(i).size();j++){
      String tmp=j+" ";
      for(int k=0;k<ListLog.get(i).get(j).size();k++){
        tmp = tmp+ListLog.get(i).get(j).get(k)+" ";
      }
      tmp=tmp+"\n";
      try {
        fw.write(tmp);
      }catch (IOException e) {e.printStackTrace();}
    }
  }

  //Fonction qui écris le fichier GNUplot pour les joueur
  public static void ecrireGNUplotJoueur(FileWriter fw,int i){
    for(int j=0;j<ListLogJoueur.get(i).size();j++){
  		String tmp=j+" ";
  		for(int k=0;k<ListLogJoueur.get(i).get(j).size();k++){
  			tmp = tmp+ListLogJoueur.get(i).get(j).get(k)+" ";
  		}
  		tmp=tmp+"\n";
  		try {
  			fw.write(tmp);
  		  }catch (IOException e) {e.printStackTrace();}
	   }
  }

  //Fonction qui écris le fichier GNUplot pour l'avancement de chaque joueur
  public static void ecrireGNUplotGlobal(FileWriter fw){
    ArrayList<ArrayList<Integer>> ListGL = new ArrayList<ArrayList<Integer>>();
    for(int i=0;i<ListJoueur.size();i++){
      ListGL.add(ListJoueur.get(i).getProgress());
    }
    int max = maxAction();
    int objectifTotal = ListRessources.size()*Objectif;
    for(int i=0;i<max;i++){
      String tmp=i+" ";
      for(int j=0;j<ListGL.size();j++){
        if(i >= ListGL.get(j).size()){
          tmp=tmp+(double)ListGL.get(j).get(ListGL.get(j).size()-1)/(double)objectifTotal+" ";
        }else{
          tmp=tmp+(double)ListGL.get(j).get(i)/(double)objectifTotal+" ";
        }
      }
      tmp=tmp+"\n";
  		//System.out.print(tmp);
  		try {
  			fw.write(tmp);
  		}catch (IOException e) {e.printStackTrace();}
    }

  }

  //Renvoie le nombre d'action maximal effectué par les joueur
  private static int maxAction(){
    ArrayList<ArrayList<Integer>> ListGL = new ArrayList<ArrayList<Integer>>();
    for(int i=0;i<ListJoueur.size();i++){
      ListGL.add(ListJoueur.get(i).getProgress());
    }
    int max=0;
    for(int i=0;i<ListGL.size();i++){
      if(max < ListGL.get(i).size())
        max=ListGL.get(i).size();
    }
    return max;
  }

  //Ajoute l'état de la ressource "name" dans les log
  public static void addLog(String name){
	  ArrayList<Integer> tmpList=new ArrayList<Integer>();
	  for(int i=0;i<ListJoueur.size();i++){
		  int tmp =ListJoueur.get(i).lastAction(name);
		  if(tmp==-1)
			tmp=0;
		  tmpList.add(tmp);
	  }
	  ListLog.get(indexRessources(name)).add(tmpList);
  }

  //Ajoute l'etat du joueur "name" dans les log
  public static void addLogJoueur(String name){
	  //System.out.println(name);
	  ArrayList<Integer> tmpList=new ArrayList<Integer>();
	  ArrayList<RessourcesLog> StockRessources = findJoueur(name).getStock();
	  for(int i=0;i<StockRessources.size();i++){
		  int tmp =StockRessources.get(i).lastAction();
		  if(tmp==-1)
			tmp=0;
		  tmpList.add(tmp);
	  }
	  ListLogJoueur.get(indexJoueur(name)).add(tmpList);
  }


}
