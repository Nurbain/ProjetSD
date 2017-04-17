import java.rmi.Remote ;
import java.rmi.RemoteException ;

public interface ObservateurInterface extends Remote // Hérite de Remote
{

  public void generationLog()
  throws RemoteException;

  public void start()
  throws RemoteException;

}
