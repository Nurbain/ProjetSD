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


	public boolean DemandeRessource(int nbr)
	{
		
		return true;
	}

	synchronized public boolean VolRessource(Ressources r)
	{

		return true;
	}

	public void AskAction()
	{
		/*Affichage Menu du choix d'action*/
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
			if(this.StockRessources.get(i).getName().equals(nom))
				return i;

		return index;
	}

}
