import java.rmi.Remote ;
import java.rmi.RemoteException ;
import java.util.*;

public interface JoueurInterface extends Remote // Hérite de Remote
{

  public ArrayList<Ressources> GetStock()
  throws RemoteException;

  public Personnalite GetPersonnalite()
  throws RemoteException;

  public Mode GetMode()
  throws RemoteException;

  public boolean GetisJoueurIRL()
  throws RemoteException;

  public boolean VolRessourceAgresseur(Joueur j, Ressources r)
  throws RemoteException;

	public int VolRessourceVictime(Ressources r)
 	throws RemoteException;

}
