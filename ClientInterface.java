import java.rmi.Remote ;
import java.rmi.RemoteException ;
import java.util.*;

public interface ClientInterface extends Remote // Hérite de Remote
{
	public String getName()
	throws RemoteException ;
	public void ConnexionPeer(String ServerName,String PortNum,String namePeer,Type typePeers)
	throws RemoteException;
	public Type getmonType()
	throws RemoteException;
	public int PrendreRessource()
  throws RemoteException;

	public void startAgent()
	throws RemoteException;

  public Ressources GetRessources()
  throws RemoteException;

  public int GetCanGive()
	throws RemoteException;

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

	public void generationLog()
  throws RemoteException;

  public void start()
  throws RemoteException;
}
