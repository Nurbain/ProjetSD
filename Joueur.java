import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Joueur extends Client {

	private Client myClient;
	private Personnalite perso;
	private List<Ressources> StockRessources = new ArrayList<>();;
	private boolean isJoueurIRL;
	private boolean VolAllow;
	private Mode mode = Mode.Demande;
	
	
	public Joueur(String name, Client myclient, Personnalite perso, boolean isIRL, boolean allow ) throws RemoteException{
		super(name);
		
		this.myClient = myclient;
		this.perso = perso;
		this.isJoueurIRL = isIRL;
		this.VolAllow = allow;
	}
	
	
	
	public List<Ressources> GetStock()
	{
		return this.StockRessources;
	}
	
	public Personnalite GetPersonnalite()
	{
		return this.perso;
	}
	
	public Mode GetMode()
	{
		return this.mode;
	}
	
	public boolean GetisJoueurIRL()
	{
		return this.isJoueurIRL;
	}
	
	public void SetPersonnalite(Personnalite perso)
	{
		this.perso = perso;
	}
	
	public void SetIsJoueur(boolean b)
	{
		this.isJoueurIRL = b;
	}
	
	public void SetMode(Mode m)
	{
		this.mode = m;
	}
	
	public Client GetClient()
	{
		return this.myClient;
	}
	
	
	public boolean DemandeRessource(Producteur p , int nbr)
	{
		
		return true;
	}
	
	public void AjoutStock(int nbr, Ressources r)
	{
		
	}
	
	public boolean SupprStock(int nbr, Ressources r)
	{
		return true;
	}
	
	public boolean VolRessource(Joueur j, Ressources r)
	{
		
		return true;
	}
	
	public void AskAction()
	{
		
	}
	
	public void Observation(Joueur j)
	{
		
	}
	
	private int SearchRessource(Ressources r)
	{
		int index = -1;
		
		if(this.StockRessources.isEmpty())
			return index;
		
		for(int i = 0; i<this.StockRessources.size() ; i++)
			if(r==this.StockRessources.get(i))
				return i=index;
		
		return index;
	}
	
}

