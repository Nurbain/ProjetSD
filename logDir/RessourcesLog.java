import java.util.ArrayList;

/**@author WENDLING Quentin URBAIN Nathan
*/

public class RessourcesLog {
  //Nom de la ressource
  public final String name;
  //Historique des actions sur la ressource
  private ArrayList<Integer> historique = new ArrayList<Integer>();

  public RessourcesLog(String name,int nbInit){
    this.name=name;
    //Initialisation de l'historique
    this.historique.add(nbInit);
  }

  //Fonction qui ajoute nb exemplaires à la ressource
  public void add(int nb){
    this.historique.add(this.historique.get(this.historique.size()-1)+nb);
  }

  //Fonction qui retire nb exemplaire à la ressource
  public void sub(int nb){
    this.historique.add(this.historique.get(this.historique.size()-1)-nb);
  }

  //Fonction qui renvoie l'historique de la ressource
  public ArrayList<Integer> getHistorique(){
    return this.historique;
  }

  //Fonction qui renvoie la dernière action effectuer dans la ressource
  public int lastAction(){
	  return historique.get(historique.size()-1);
  }

}
