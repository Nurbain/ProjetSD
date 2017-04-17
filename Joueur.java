import java.rmi.RemoteException;
import java.util.ArrayList;

public class Joueur extends Client implements JoueurInterface{

	static final long serialVersionUID = 42;
	
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
				
				//Poke l'observateur
				
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
					
				
				//Poke l'observateur 
				
				break;
				
				
			//Ne fait que voler aux autres joueurs
			case Voleur :
				//Change pour observer le producteur de la ressource 
				if(GetMode() != Mode.Observation)
					SetMode(Mode.Observation);
				
				
				int indexJoueur = 0 , indexRessource2 = -1;;
				for(int i = 0; i<StockRessources.size() ; i++)
				{
					//Trouve les ressources qui n'ont pas atteint l'objectif
					if(StockRessources.get(i).getExemplaires() < objectif)
					{
						indexRessource2 = i;
						break;
					}
				}
				
				//Cherche les joueurs ayant cette ressources en plus grand nombre
				int max = 0;
				for(int j = 0; j<this.ListJoueur.size() ; j++)
				{
					//Recupère les stocks des joueurs 
					ArrayList<Ressources> tmp = null;
					try {tmp = Observation(this.ListJoueur.get(j));} 
					catch (RemoteException re) {System.out.println(re);}
					
					if(tmp != null)
					{
						for(int k =0; k<tmp.size();k++)
						{
							//Regarde si le joueurs possède la ressource , en plus grand nombre que le précédent
							if(tmp.get(k).equals(this.StockRessources.get(indexRessource2)) && tmp.get(k).getExemplaires() > max)
							{
								max = tmp.get(k).getExemplaires();
								//Le joueur actuelle est le plus interressent a voler
								indexJoueur = j;
							}
						}
					}
				}
				
				SetMode(Mode.Vol);
				
				try {VolRessourceAgresseur(this.ListJoueur.get(indexJoueur), StockRessources.get(indexRessource2));} 
				catch (RemoteException re) {System.out.println(re);}
						
				
				//Poke l'observateur
				
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
		////Verifie que le joueur est bien en mode demande pour demander
		if(GetMode() != Mode.Demande)
			return false;
		
		Ressources NewRessource = p.GetStock();
		int nbr = p.PrendreRessource();
		
		return this.AjoutStock(NewRessource , nbr);
	}

	
	//Donne le nombre pouvant etre vole
	synchronized public int VolRessourceVictime(Ressources r)
	{
		//Si le joueur est en mode protection marche pas 
		if(GetMode() == Mode.Protection)
			return 0;
		
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
	synchronized public boolean VolRessourceAgresseur(JoueurInterface j, Ressources r ) throws RemoteException
	{
		//Verifie que le voleur est bien en mode vol
		if(GetMode() != Mode.Vol)
			return false;
		
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
	synchronized public ArrayList<Ressources> Observation(JoueurInterface j) throws RemoteException
	{
		if(GetMode() == Mode.Observation)
			return j.GetStock();
		else return null;	
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
