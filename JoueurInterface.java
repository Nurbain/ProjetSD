
import java.rmi.RemoteException ;
import java.util.*;

public interface JoueurInterface extends ClientInterface // Hérite de Remote
{

  public ArrayList<Ressources> GetStock()
  throws RemoteException;

  public Personnalite GetPersonnalite()
  throws RemoteException;

  public Mode GetMode()
  throws RemoteException;

  public boolean GetisJoueurIRL()
  throws RemoteException;

  public boolean VolRessourceAgresseur(JoueurInterface j, Ressources r)
  throws RemoteException;

  public int VolRessourceVictime(Ressources r)
		  throws RemoteException;

	public ArrayList<Ressources> Observation(JoueurInterface j)
	throws RemoteException;

}
