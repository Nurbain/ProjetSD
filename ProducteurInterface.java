import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface ProducteurInterface extends Remote // Hérite de Remote
{

  public boolean PrendreRessource(int nbr)
  throws RemoteException;

  public Ressources GetStock()
  throws RemoteException;

}
