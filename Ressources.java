public class Ressources {
  
  private final String name;
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

  public synchronized Boolean takeRessources(int nb){
		if(nb > exemplaires){
		  return false;
		}
		exemplaires -= nb;
		return true;
  }

  public synchronized void addRessources(int nb){
    exemplaires += nb;
  }
}