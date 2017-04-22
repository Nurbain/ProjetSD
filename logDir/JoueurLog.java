import java.util.ArrayList;

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
    return null;
  }

  public void add(String name,int nb){
    findRessources(name).add(nb);
  }

  public void sub(String name,int nb){
    findRessources(name).sub(nb);
  }

}
