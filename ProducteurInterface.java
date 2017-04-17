import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface ProducteurInterface extends Remote // Hérite de Remote
{

  public int PrendreRessource()
  throws RemoteException;

  public Ressources GetStock()
  throws RemoteException;
  
  public int GetCanGive()
	throws RemoteException;

}
