import java.util.ArrayList;
import java.io.*;

public class GenerateurLog{
  private static ArrayList<JoueurLog> ListJoueur = new ArrayList<JoueurLog>();
  private static ArrayList<ProducteurLog> ListProducteur = new ArrayList<ProducteurLog>();
  private static ArrayList<RessourcesLog> ListRessources = new ArrayList<RessourcesLog>();
  private static ArrayList<ArrayList<ArrayList<Integer>>> ListLog = new ArrayList<ArrayList<ArrayList<Integer>>>();
  private static ArrayList<ArrayList<ArrayList<Integer>>> ListLogJoueur = new ArrayList<ArrayList<ArrayList<Integer>>>();
  private static String mode;
  private static int Objectif;

  public static void main(String[] args){
    if (args.length != 2)
		{
			System.out.println("Usage : java GenerateurLog <nom du fichier de log> <destination>") ;
			System.exit(0) ;
		}
    BufferedReader bis =null;
    FileWriter fw=null;
    try {
		    fw = new FileWriter(new File(args[1]+"0"));
	   }
    catch (IOException e) {e.printStackTrace();}

    try{
      bis = new BufferedReader(new FileReader(new File(args[0])));
      String line;
      LectureMode(bis);
      System.out.println("Mode = "+mode);
      LectureObjectif(bis);
      System.out.println("Objectif = "+Objectif);
      while((line = bis.readLine()) != null){

        System.out.println(line);
        if(line.equals("Producteurs :")){
          System.out.println("Liste Prod :");
          LectureProducteur(bis);
          break;
        }
        if(line.equals("Joueurs :")){
          System.out.println("Liste Joueur :");
          LectureJoueur(bis);

        }

      }
      for(int i=0;i<ListRessources.size();i++){
		  ListLog.add(new ArrayList<ArrayList<Integer>>());
	  }
	  for(int i=0;i<ListJoueur.size();i++){
		  ListLogJoueur.add(new ArrayList<ArrayList<Integer>>());
	  }
      while((line = bis.readLine()) != null){
        LectureAction(line);
      }
    }
    catch (FileNotFoundException e){e.printStackTrace();}
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
    try {
		    FileWriter fw2 = new FileWriter(new File("tmpParam"));
        fw2.write(""+ListJoueur.size());
        fw2.close();
	   }
    catch (IOException e) {e.printStackTrace();}
    try {
		    FileWriter fw3 = new FileWriter(new File("tmpParam2"));
        fw3.write(""+ListRessources.size());
        fw3.close();
	   }
    catch (IOException e) {e.printStackTrace();}
    ecrireGNUplot(fw,0);
    try {
      fw.close();
    }
    catch (IOException e) {e.printStackTrace();}
    for(int j=1;j<ListRessources.size();j++){
		try {
		    fw = new FileWriter(new File(args[1]+j));
		   }
		catch (IOException e) {e.printStackTrace();}
		ecrireGNUplot(fw,j);
		try {
			fw.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}

	for(int j=0;j<ListJoueur.size();j++){
		try {
		    fw = new FileWriter(new File(args[1]+"J"+j));
		   }
		catch (IOException e) {e.printStackTrace();}
		ecrireGNUplotJoueur(fw,j);
		try {
			fw.close();
		}
		catch (IOException e) {e.printStackTrace();}
	}
  try {
      fw = new FileWriter(new File(args[1]+"GL"));
     }
  catch (IOException e) {e.printStackTrace();}
  ecrireGNUplotGlobal(fw);
  try {
    fw.close();
  }
  catch (IOException e) {e.printStackTrace();}

  }

  public static void LectureProducteur(BufferedReader bis){
    try{
      String line;
      while((line = bis.readLine()) != null){
        if(line.equals("")){
          System.out.println("Fin des prod");
          return;
        }
        String delims = "[ ]+";
        String[] tokens = line.split(delims);
        ListProducteur.add(new ProducteurLog(tokens[0],tokens[1],Integer.parseInt(tokens[2])));
        if(!existRessources(tokens[1])){
          ListRessources.add(new RessourcesLog(tokens[1],0));
        }
      }
    }catch (IOException e){e.printStackTrace();}

  }

  public static void LectureJoueur(BufferedReader bis){
    try{
      String line;
      while((line = bis.readLine()) != null){
        if(line.equals("")){
          System.out.println("Fin des joueur");
          return;
        }
        ListJoueur.add(new JoueurLog(line));
      }
    }catch (IOException e){e.printStackTrace();}

  }

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

  public static int indexRessources(String name){
	  for(int i=0;i<ListRessources.size();i++){
		  if(ListRessources.get(i).name.equals(name))
			return i;
	  }
	  return -1;
  }

  public static int indexJoueur(String name){
	  for(int i=0;i<ListJoueur.size();i++){
		  if(ListJoueur.get(i).name.equals(name))
			return i;
	  }
	  return -1;
  }

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

  public static void LectureAction(String line){
    String delims = "[ ]+";
    String[] tokens = line.split(delims);
    
    if(tokens[0].equals("Joueur")){
      System.out.println("Action Joueur "+tokens[1]+" "+tokens[2]);
      JoueurLog jTmp=findJoueur(tokens[1]);
      
      if(tokens[2].equals("Prend")){
        System.out.println("Prend ");
        jTmp.add(tokens[3],Integer.parseInt(tokens[4]));
        addLog(tokens[3]);
        addLogJoueur(tokens[1]);
        findProducteur(tokens[6]).sub(Integer.parseInt(tokens[4]));

      }
      else if(tokens[2].equals("Punit"))
      {
    	  System.out.println("Punit ");
    	  JoueurLog JpTmp = findJoueur(tokens[6]);
    	  JpTmp.sub(tokens[3], Integer.parseInt(tokens[4]));
    	  addLogJoueur(tokens[6]);
      }
      else if(tokens[2].equals("Vol"))
      {
    	  System.out.println("Vol ");
    	  jTmp.add(tokens[3],Integer.parseInt(tokens[4]));
    	  addLogJoueur(tokens[1]);
    	  findJoueur(tokens[6]).sub(tokens[3],Integer.parseInt(tokens[4]));
      }
    }else if(tokens[0].equals("Producteur")){
      System.out.println("Action Producteur "+tokens[1]);
      ProducteurLog pTmp=findProducteur(tokens[1]);
      if(tokens[2].equals("Produit")){
        System.out.println("Produit");
        pTmp.add(Integer.parseInt(tokens[4]));
      }
    }

  }

  public static boolean existRessources(String name){
    for(RessourcesLog r : ListRessources){
      if(r.name.equals(name))
        return true;
    }
    return false;
  }

  public static int nbActionRessourcemax(String name){
    int tmp,max=0;
    for(int i=0;i<ListJoueur.size();i++){
      tmp = ListJoueur.get(i).nbActionRessource(name);
      if(tmp>max)
        max=tmp;
    }
    return max;
  }

  public static void ecrireGNUplot(FileWriter fw,int i){
    System.out.println("nb joueur : "+ListJoueur.size());
    for(int j=0;j<ListLog.get(i).size();j++){
		String tmp=j+" ";
		for(int k=0;k<ListLog.get(i).get(j).size();k++){
			tmp = tmp+ListLog.get(i).get(j).get(k)+" ";
		}
		tmp=tmp+"\n";
		System.out.print(tmp);
		try {
			fw.write(tmp);

		  }
		  catch (IOException e) {e.printStackTrace();}
	}

  }

  public static void ecrireGNUplotJoueur(FileWriter fw,int i){
    for(int j=0;j<ListLogJoueur.get(i).size();j++){
  		String tmp=j+" ";
  		for(int k=0;k<ListLogJoueur.get(i).get(j).size();k++){
  			tmp = tmp+ListLogJoueur.get(i).get(j).get(k)+" ";
  		}
  		tmp=tmp+"\n";
  		System.out.print(tmp);
  		try {
  			fw.write(tmp);

  		  }
  		  catch (IOException e) {e.printStackTrace();}
	   }

  }

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
  		System.out.print(tmp);
  		try {
  			fw.write(tmp);
  		}catch (IOException e) {e.printStackTrace();}
    }

  }

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

  public static void addLog(String name){
	  System.out.println(name);
	  ArrayList<Integer> tmpList=new ArrayList<Integer>();
	  for(int i=0;i<ListJoueur.size();i++){
		  int tmp =ListJoueur.get(i).lastAction(name);
		  if(tmp==-1)
			tmp=0;
		  tmpList.add(tmp);
	  }
	  ListLog.get(indexRessources(name)).add(tmpList);
  }

  public static void addLogJoueur(String name){
	  System.out.println(name);
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
