import java.io.Serializable;

/**@author WENDLING Quentin URBAIN Nathan*/

public class Ressources implements Serializable{

  static final long serialVersionUID = 42;
  
	//Nom de la ressource 
	private final String name;

	//Nombre d'exemplaire de la ressource
  private int exemplaires;

  public Ressources(String name, int initnbr)
  {
	  this.name = name;
	  this.exemplaires = initnbr;
  }

  public String getName(){
	  return name;
  }

  public int getExemplaires(){
    return this.exemplaires;
  }

	//Soustrait au nombre d'exemplaire le nombre donne arguement
  public synchronized Boolean takeRessources(int nb){
		if(nb > exemplaires){
		  return false;
		}
		exemplaires -= nb;
		return true;
  }

	//Ajoute au nombre d'exemplaire le nombr donne en arguement
  public synchronized void addRessources(int nb){
    exemplaires += nb;
  }

	//Test si une ressource passe en commentaire est eguale a l'actuel
  public boolean equals(Ressources r2){
	  return r2.name.equals(this.name);
  }
}
