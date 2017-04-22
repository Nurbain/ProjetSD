
public class ProducteurLog {
  public final String name;
  private RessourcesLog stock;

  public ProducteurLog(String name,String nameRessource, int nbrinit){
    this.name=name;
    stock=new RessourcesLog(nameRessource,nbrinit);
  }

  public void add(int nb){
    this.stock.add(nb);
  }

  public void sub(int nb){
    this.stock.sub(nb);
  }

}
