import java.util.ArrayList;

public class RessourcesLog {
  public final String name;
  private ArrayList<Integer> historique = new ArrayList<Integer>();

  public RessourcesLog(String name,int nbInit){
    this.name=name;
    this.historique.add(nbInit);
  }

  public void add(int nb){
    this.historique.add(this.historique.get(this.historique.size()-1)+nb);
  }

  public void sub(int nb){
    this.historique.add(this.historique.get(this.historique.size()-1)-nb);
  }

  public ArrayList<Integer> getHistorique(){
    return this.historique;
  }

}
