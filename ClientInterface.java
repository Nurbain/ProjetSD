import java.rmi.Remote ;
import java.rmi.RemoteException ;
import java.util.*;

/**@author WENDLING Quentin URBAIN Nathan*/

//Interface de la classe Client
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

	public int SommeRessources()
	throws RemoteException;

	public boolean getAfini()
	throws RemoteException;

	public void disconnect()
	throws RemoteException;

	public ArrayList<LogEntries> getLogPerso()
	throws RemoteException;

	public void startAgent()
	throws RemoteException;

	public void PartieFini(String name)
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

  public int Getobjectif()
throws RemoteException;

	public void DonnerAmende(Ressources r,int amende)
	throws RemoteException;

  public int VolRessourceVictime(Ressources r)
	throws RemoteException;

	public void generationLog()
  throws RemoteException;

  public void start()
  throws RemoteException;

	public void generationLog(String nameEmetteur,Type typeEmetteur,Action event)
	throws RemoteException;

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,Ressources r,int nombre)
	throws RemoteException;

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,Ressources r,int nombre,String nameReceveur,Type typeReceveur)
	throws RemoteException;

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,String nameReceveur,Type typeReceveur)
	throws RemoteException;

	public void tourDeJeu()
	throws RemoteException;

	public void PartieFini()
	throws RemoteException;

}
