/**@author WENDLING Quentin URBAIN Nathan
*/

public class ProducteurLog {
  //Nom du Producteur
  public final String name;
  //Historique de la ressource produite par le producteur
  private RessourcesLog stock;

  public ProducteurLog(String name,String nameRessource, int nbrinit){
    this.name=name;
    //On initialise la ressource du producteur
    stock=new RessourcesLog(nameRessource,nbrinit);
  }

  //Ajoute nb exemplaires à la ressource
  public void add(int nb){
    this.stock.add(nb);
  }

  //Retire nb exemplaires à la ressource
  public void sub(int nb){
    this.stock.sub(nb);
  }

}
