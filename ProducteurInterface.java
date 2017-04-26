
import java.rmi.RemoteException ;

public interface ProducteurInterface extends ClientInterface // Hérite de Remote
{

  public int PrendreRessource()
  throws RemoteException;

  public Ressources GetRessources()
  throws RemoteException;

  public int GetCanGive()
	throws RemoteException;

}
