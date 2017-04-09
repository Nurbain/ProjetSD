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



	public ArrayList<Ressources> GetStock()
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

	public void SetMode(Mode m)
	{
		this.mode = m;
	}


	public boolean DemandeRessource(int nbr)
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

	public boolean VolRessource(Ressources r)
	{

		return true;
	}

	public void AskAction()
	{

	}

	public void Observation()
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
