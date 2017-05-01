import java.rmi.server.UnicastRemoteObject ;
import java.rmi.RemoteException ;
import java.rmi.* ;
import java.net.MalformedURLException ;
import java.util.*;

/**@author WENDLING Quentin URBAIN Nathan*/

public class Client
extends UnicastRemoteObject // Hérite de UnicastRemoteObject
implements ClientInterface, Runnable // implémente l’interface
{
	static final long serialVersionUID = 42;

	//Nom du client
	protected String name;

	//Type du clinet
	protected Type monType;

	//Tout les autres clients avec les quels je suis connecté
	protected ArrayList<ClientInterface> Peers= new ArrayList<ClientInterface>();

	//Tous les joueurs de la partie
	protected ArrayList<ClientInterface> ListJoueur =new ArrayList<ClientInterface>();

	//Tous les producteurs de la partie
	protected ArrayList<ClientInterface> ListProducteur =new ArrayList<ClientInterface>();

	//Liste de Log personel
	protected ArrayList<LogEntries> LogPerso = new ArrayList<LogEntries>();

	//Observateur de la partie
	protected ClientInterface obs;

	protected boolean finParti=false;
	private final String ServerName;
	private final String NumPort;
	private final String NomServise;

	//Variable permettant de connaitre la duree de la partie
	protected long StartTimer;


	public Client (String name,String ServerName,String NumPort,String NomServise) throws RemoteException
	// Rmq : Le client n’a pas accès au constructeur
	{
		super() ;
		this.name=name;
		this.monType=Type.Client;
		this.ServerName=ServerName;
		this.NumPort=NumPort;
		this.NomServise=NomServise;
	} ;

	//Retourne le nom du client
	public String getName() throws RemoteException
	{
		return this.name;
	}

	//Retourne son log personnel
	public ArrayList<LogEntries> getLogPerso() throws RemoteException{
		return this.LogPerso;
	}

	//Retourne son type
	public Type getmonType(){
		return this.monType;
	}

	public void tourDeJeu(){

	}

	public void PartieFini(){
		this.finParti=true;
	}

	//Fonction deconnectant le client
	public void disconnect() throws RemoteException{
		try
    {
	  	Naming.unbind("rmi://"+ServerName+":" + NumPort + "/" + NomServise );
			System.out.println("Fin serv");
			System.exit(0) ;
    }
    catch (NotBoundException re) { System.out.println(re) ; }
    catch (MalformedURLException e) { System.out.println(e) ; }

	}

	//Fonction lancant le client
	public void startAgent(){
		System.out.println("Start");
		Thread t=new Thread(this);
		t.start();
	}

	public void run() {
	}

	public void PartieFini(String name){
	}

	/*Ajoute un client au client avec les quels je suis connecté
		namePeer Nom du client
		Cherche sur rmi://<ServerName>:<NumPort>/<namePeer>*/
	public void ConnexionPeer(String ServerName,String PortNum,String namePeer,Type typePeers) throws RemoteException {
		try
		{
		  System.out.println("Connexion a : "+namePeer);
		  //Me connecte au client passé en paramettre
			switch(typePeers){
				case Observateur:
					ClientInterface a = (ClientInterface) Naming.lookup( "rmi://"+ServerName+":"+PortNum+"/"+namePeer ) ;
					obs = a;
					return;
				case Joueur:
					ClientInterface b = (ClientInterface) Naming.lookup( "rmi://"+ServerName+":"+PortNum+"/"+namePeer ) ;
					ListJoueur.add(b);
					return;
				case Producteur:
					ClientInterface c = (ClientInterface) Naming.lookup( "rmi://"+ServerName+":"+PortNum+"/"+namePeer ) ;
					System.out.println("Ajout prod");
					ListProducteur.add(c);
					return;
				case Client:
					return;
			}
		}
		catch (NotBoundException re) { System.out.println(re) ; }
		catch (RemoteException re) { System.out.println(re) ; }
		catch (MalformedURLException e) { System.out.println(e) ; }
	}

	public int PrendreRessource(){
		return 0;
	}

  public Ressources GetRessources(){
		return null;
	}

  public int GetCanGive(){
		return 0;
	}

	public ArrayList<Ressources> GetStock(){
		return null;
	}

	public void DonnerAmende(Ressources r,int amende){
	}

  public Personnalite GetPersonnalite(){
		return Personnalite.Voleur;
	}


  public Mode GetMode(){
		return Mode.Vol;
	}
  public boolean GetisJoueurIRL(){
		return false;
	}


  public int VolRessourceVictime(Ressources r){
		return 0;
	}

  public int Getobjectif()
  {
	  return 0;
  }

	public void generationLog(){
	}

  public void start(){

	}

	public void generationLog(String nameEmetteur,Type typeEmetteur,Action event){
  }

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,Ressources r,int nombre){
  }

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,Ressources r,int nombre,String nameReceveur,Type typeReceveur){
  }

  public void generationLog(String nameEmetteur,Type typeEmetteur,Action event,String nameReceveur,Type typeReceveur){
  }

}
