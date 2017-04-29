package logDir;

import java.util.ArrayList;
import java.io.*;

public class JoueurLog {
  public final String name;
  private ArrayList<RessourcesLog> StockRessources = new ArrayList<RessourcesLog>();

  public JoueurLog(String name){
    this.name=name;
  }

  private RessourcesLog findRessources(String name){
    for(int i=0;i<StockRessources.size();i++){
      if(StockRessources.get(i).name.equals(name)){
        return StockRessources.get(i);
      }
    }
    System.out.println("Pas trouve");
    return null;
  }

  public void add(String name,int nb){
	System.out.println("add :"+name);
    if(findRessources(name) == null){
		System.out.println("creation :"+name);
      StockRessources.add(new RessourcesLog(name,0));
	}
    findRessources(name).add(nb);
  }

  public void sub(String name,int nb){
    findRessources(name).sub(nb);
  }

  public ArrayList<RessourcesLog> getStock(){
    return this.StockRessources;
  }

  public boolean ecritureLog(FileWriter fw,String nomRessource,int i){
    RessourcesLog r=findRessources(nomRessource);

    if(i >= r.getHistorique().size()-1){
      try {
        System.out.println(r.getHistorique().get(r.getHistorique().size()-1));
    		fw.write(""+r.getHistorique().get(r.getHistorique().size()-1));
        return false;
    	}
    	catch (IOException e) {e.printStackTrace();return false;}
    }
    try {
      System.out.println(r.getHistorique().get(i));
      fw.write(""+r.getHistorique().get(i));
      return true;
    }
    catch (IOException e) {e.printStackTrace();return true;}

  }

  public int nbActionRessource(String name){
    return findRessources(name).getHistorique().size();
  }
  
  public int lastAction(String name){
	  if(findRessources(name) == null)
		return -1;
	  return findRessources(name).lastAction();
  }

}
