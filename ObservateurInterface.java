
import java.rmi.RemoteException ;

public interface ObservateurInterface extends ClientInterface // Hérite de Remote
{

  public void generationLog()
  throws RemoteException;

  public void start()
  throws RemoteException;

}
