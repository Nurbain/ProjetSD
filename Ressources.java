public class Ressources {
  public final name;
  private int exemplaires;

  public int getExemplaires(){
    return this.exemplaires;
  }

  public bool takeRessources(int nb){
    if(nb > exemplaires){
      return false;
    }
    exemplaires -= nb;
    return true;
  }

  public void addRessources(int nb){
    exemplaires += nb;
  }
}
