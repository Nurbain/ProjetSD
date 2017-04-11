import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Joueur extends Client implements JoueurInterface{

	private Personnalite perso;
	private ArrayList<Ressources> StockRessources = new ArrayList<Ressources>();;
	private boolean isJoueurIRL;
	private Mode mode = Mode.Demande;


	public Joueur(String name, Personnalite perso, boolean isIRL ) throws RemoteException{
		super(name);

		this.perso = perso;
		this.isJoueurIRL = isIRL;
	}



	synchronized public ArrayList<Ressources> GetStock()
	{
		return this.StockRessources;
	}

	public Personnalite GetPersonnalite()
	{
		return this.perso;
	}

	synchronized public Mode GetMode()
	{
		return this.mode;
	}

	public boolean GetisJoueurIRL()
	{
		return this.isJoueurIRL;
	}

	public void SetMode(Mode m)
	{
		this.mode = m;
	}


	public boolean DemandeRessource(Producteur p)
	{
		int StockPris = p.PrendreRessource();
		String NomNewRessource = p.GetStock().getName();
		int indexStock = SearchRessource(NomNewRessource);
		
		if(indexStock == -1)
		{
			Ressources r1 = new Ressources(NomNewRessource,StockPris);
			StockRessources.add(r1);
		}
		else
		{
	/*Mon cerveau a brainlag donc je crois qu'on peut faire plus simple faut que je verifie*/
			Ressources r2 = StockRessources.get(indexStock);
			r2.addRessources(StockPris);
			StockRessources.set(indexStock, r2);			
		}

		return true;
	}

	synchronized public int VolRessource(Joueur j)
	{
		
		
		return 0;
	}

	public void AskAction()
	{

	}

	public void Observation()
	{

	}


	private int SearchRessource(String nom)
	{
		int index = -1;

		if(this.StockRessources.isEmpty())
			return index;

		for(int i = 0; i<this.StockRessources.size() ; i++)
			if(this.StockRessources.get(i).getName().equalsIgnoreCase(nom))
				return i;

		return index;
	}

}
