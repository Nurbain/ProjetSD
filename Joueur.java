import java.rmi.RemoteException;
import java.util.ArrayList;

public class Joueur extends Client implements JoueurInterface{

	private Personnalite perso;
	private ArrayList<Ressources> StockRessources = new ArrayList<Ressources>();;
	private boolean isJoueurIRL;
	private Mode mode = Mode.Demande;
	private int objectif;


	public Joueur(String name, Personnalite perso, boolean isIRL , int objectif ) throws RemoteException{
		super(name);

		this.perso = perso;
		this.isJoueurIRL = isIRL;
		this.objectif = objectif;
	}


	public void run() {
		while(true){	
			
			//Récupère la personnalite du joueur
			Personnalite perso = this.perso;	
			
			switch(perso)
			{
			
			//Prends pour chaque ressources le nombre a avoir pour finir
			case Individuel :
				
				//Change le mode si besoin en demande
				if(GetMode() != Mode.Demande)
					SetMode(Mode.Demande);
				
				//Boucle qui regarde les ressources qui n'ont pas atteint l'objectif
				int indexRessource1 = -1;
				for(int i = 0; i<StockRessources.size() ; i++)
				{
					if(StockRessources.get(i).getExemplaires() < objectif)
					{
						indexRessource1 = i;
						break;
					}
				}
				
				//Recupere le producteur de la ressources manquante
				int indexProd1 = SearchProducteur(StockRessources.get(indexRessource1));
				
				//Demande la ressource 
				try {DemandeRessource(this.ListProducteur.get(indexProd1));} 
					catch (RemoteException re) {System.out.println(re);}
				
				break;
			
				
			//Prends seulement si le producteur possède au moins une demande "entiere" de ressources
			case Cooperatif :
				
				//Change pour observer le producteur de la ressource 
				if(GetMode() != Mode.Observation)
					SetMode(Mode.Observation);
				
		
				int indexProd2 = -1;
				for(int i = 0; i<StockRessources.size() ; i++)
				{
					//Trouve les ressources qui n'ont pas atteint l'objectif
					if(StockRessources.get(i).getExemplaires() < objectif)
					{
						//Cherche les producteurs pouvant en donner 
						indexProd2 = SearchProducteur(StockRessources.get(i));
						
						//Verifie que le producteur selectionne possede au moins ce qu'il peut donner et pas moins
						try {
							if(this.ListProducteur.get(indexProd2).GetStock().getExemplaires() > this.ListProducteur.get(indexProd2).GetCanGive())
								//Le producteur peut donner , il a assez , donc on quitte la boucle
								break;
							} 
						catch (RemoteException re) {System.out.println(re);}
					}
				}
				
				//Passe en mode demande
				SetMode(Mode.Demande);
				try {DemandeRessource(this.ListProducteur.get(indexProd2));} 
					catch (RemoteException re) {System.out.println(re);}
					
				break;
				
				
			//Ne fait que voler aux autres joueurs
			case Voleur :
				
				
				break;
				
			/*Personnalite a rajouter*/
				
			default:
				break;
			}
			//ThreadSleep ? 
		}
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


	public boolean DemandeRessource(ProducteurInterface p) throws RemoteException
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
		{
			int tmp = 	StockRessources.get(index).getExemplaires();
			StockRessources.get(index).takeRessources(tmp);
			return tmp;
		}
	}


	//Joueur Voleur regarde si il peut voler 
	synchronized public boolean VolRessourceAgresseur(Joueur j, Ressources r )
	{
		int StockPris = j.VolRessourceVictime(r);
		
		//Verifie si c'est BullShit la ressource ou si y'a pas 
		if(StockPris == 0)
			return false;
		else
			return this.AjoutStock(r, StockPris);
	}

	public void AskAction()
	{
		
	}

	//Renvoie la liste des ressources du joueur observé 
	synchronized public ArrayList<Ressources> Observation(Joueur j)
	{
		return j.StockRessources;
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
		{
			if(StockRessources.get(i).equals(r))
				return i;
		}
		
		return index;
	}
	
	private int SearchProducteur(Ressources r)
	{
		int index = -1;
		
		if(this.ListProducteur.isEmpty())
			return index;

		for(int i = 0; i<this.ListProducteur.size() ; i++)
		{
			try {if(this.ListProducteur.get(i).GetStock().equals(r))
					return i;
				} 
			catch (RemoteException re) {System.out.println(re) ;}
		}	
		return index;
	}

}
