import java.util.ArrayList;

/**@author WENDLING Quentin URBAIN Nathan
*/

public class JoueurLog {
  //Nom du joueur
  public final String name;
  //Tableau de l'historique des différentes ressources du joueur
  private ArrayList<RessourcesLog> StockRessources = new ArrayList<RessourcesLog>();
  //Historique de la progression du joueur
  private ArrayList<Integer> logProgress = new ArrayList<Integer>();

  public JoueurLog(String name){
    this.name=name;
    //On initialise la progression du joueur à 0
    logProgress.add(0);
  }

  //Fonction qui renvoie l'historique de progrssion du joueur
  public ArrayList<Integer> getProgress(){
    return this.logProgress;
  }

  /**  Fonction pour trouver une ressource par son nom
  * @param name le nom de la ressource rechercher
  * @return la ressource si trouvé null sinon
  */
  private RessourcesLog findRessources(String name){
    //On parcours le Tableau de ressources
    for(int i=0;i<StockRessources.size();i++){
      if(StockRessources.get(i).name.equals(name)){
        //Quand la ressource est trouvé on la renvoie
        return StockRessources.get(i);
      }
    }
    //Si la ressource n'est pas trouvé return null
    return null;
  }

  /** Fonction pour rajouter des exempliare d'une ressource
  * @param name nom de la ressource
  * @param nb nom d'exemplaires à rajouté
  */
  public void add(String name,int nb){
    if(findRessources(name) == null){
      //Si on ne trouve pas la ressource on la crée
      StockRessources.add(new RessourcesLog(name,0));
    }
    //On ajoute l'avancé dans la progression
    logProgress.add(logProgress.get(logProgress.size()-1)+nb);
    //On ajoute les exemplaire à la ressource concernée
    findRessources(name).add(nb);
  }

  /** Fonction pour retirer des exempliare d'une ressource
  * @param name nom de la ressource
  * @param nb nom d'exemplaires à retiré
  */
  public void sub(String name,int nb){
      if(findRessources(name) == null){
        //Si on ne trouve pas la ressource on la crée
        StockRessources.add(new RessourcesLog(name,0));
    	}
    //On ajoute l'avancé dans la progression
    logProgress.add(logProgress.get(logProgress.size()-1)-nb);
    //On retire les exemplaire à la ressource concernée
    findRessources(name).sub(nb);
  }

  //Fonction qui renvoie l'historique de toute les ressources
  public ArrayList<RessourcesLog> getStock(){
    return this.StockRessources;
  }

  //Fonction qui renvoie le nombre d'actions effectué sur la ressource de nom name
  public int nbActionRessource(String name){
    return findRessources(name).getHistorique().size();
  }

  //Renvoie la derniere action effectué sur la ressource name
  public int lastAction(String name){
	  if(findRessources(name) == null)
		  return -1;
	  return findRessources(name).lastAction();
  }

}
