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
		Ressources NewRessource = p.GetStock();
		int nbr = p.PrendreRessource();
		
		return this.AjoutStock(NewRessource , nbr);
	}

	//Donne le nombre pouvant etre vole
	synchronized public int VolRessourceVictime(Ressources r)
	{
		int index = SearchRessource(r);
		
		if(index == -1)
			return 0;
		else
			return 	StockRessources.get(index).getExemplaires();		
		
	}


	//Joueur Voleur regarde si il peut voler 
	synchronized public boolean VolRessourceAgresseur(Joueur j, Ressources r )
	{
		int StockPris = j.VolRessourceVictime(r);
		
		//Verifie si c'est BullShit la ressource ou si y'a pas 
		if(StockPris == 0)
			return true;
		else
			return this.AjoutStock(r, StockPris);
	}

	public void AskAction()
	{

	}

	public void Observation()
	{

	}

	
	
	private boolean AjoutStock(Ressources r, int nbr)
	{
		int indexStock = SearchRessource(r);	
		if(indexStock == -1)
		{
			Ressources r1 = new Ressources(r.getName(),nbr);
			StockRessources.add(r1);
		}
		else
		{
	/*Mon cerveau a brainlag donc je crois qu'on peut faire plus simple faut que je verifie*/
			Ressources r2 = StockRessources.get(indexStock);
			r2.addRessources(nbr);
			StockRessources.set(indexStock, r2);			
		}

		return true;
	}

	private int SearchRessource(Ressources r)
	{
		int index = -1;

		if(StockRessources.isEmpty())
			return index;

		for(int i = 0; i<StockRessources.size() ; i++)
			if(StockRessources.get(i).equals(r))
				return i;

		return index;
	}

}
