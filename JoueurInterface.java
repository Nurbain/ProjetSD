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

  public boolean VolRessource(Ressources r)
  throws RemoteException;

}
