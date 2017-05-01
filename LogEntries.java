import java.io.Serializable;

/**@author WENDLING Quentin URBAIN Nathan*/

//Classe d'entree de log 
public class LogEntries implements Serializable{
  static final long serialVersionUID = 42;
  
	//Temps de l'action 
	public long time;

	//Action faite
  public String action;

  public LogEntries(long time,String action){
    this.time=time;
    this.action=action;
  }
}
