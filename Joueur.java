import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

public class Joueur extends Client{

	static final long serialVersionUID = 42;

	private Personnalite perso;
	private ArrayList<Ressources> StockRessources = new ArrayList<Ressources>();
	private boolean isJoueurIRL;
	private Mode mode = Mode.Demande;
	private int objectif;
	private boolean tourParTour;


	public Joueur(String name, Personnalite perso, boolean isIRL , int objectif,boolean tourParTour ) throws RemoteException{
		super(name);

		this.perso = perso;
		this.isJoueurIRL = isIRL;
		this.objectif = objectif;
		this.monType = Type.Joueur;
		this.tourParTour = tourParTour;
	}


	public void startAgent()
	{
		System.out.println("Start");
		for(int i = 0; i< ListProducteur.size() ; i++)
		{
			try {
				StockRessources.add(new Ressources(ListProducteur.get(i).GetRessources().getName(), 0));
			}
			catch (RemoteException re) { System.out.println(re) ; }
		}
		System.out.println("Fin Init");
		if(!tourParTour){
			Thread t = new Thread(this);
			t.start();
		}
	}


	public void tourDeJeu(){
		//Recupere la personnalite du joueur
		Personnalite perso = this.perso;

		switch(perso)
		{

		//Prends pour chaque ressources le nombre a avoir pour finir
		case Individuel :

			//Change le mode si besoin en demande
			if(GetMode() != Mode.Demande)
				SetMode(Mode.Demande);

			//Boucle qui regarde les ressources qui n'ont pas atteint l'objectif
			int indexRessource1 = -1 , indexProd1 = -1;
			for(int i = 0; i<StockRessources.size() ; i++)
			{
				if(StockRessources.get(i).getExemplaires() < objectif)
				{
					indexRessource1 = i;

					//Recupere le producteur de la ressources manquante
					indexProd1 = SearchProducteur(StockRessources.get(indexRessource1));

					//Verifie qu'il n'a pas 0
					try {
						if(this.ListProducteur.get(indexProd1).GetRessources().getExemplaires() > 0)
						{
							//Si pas 0 alors il prend
							break;
						}
					}
					catch (RemoteException re) {System.out.println(re);}
				}
			}

			//Demande la ressource
			try {DemandeRessource(this.ListProducteur.get(indexProd1));
				}
				catch (RemoteException re) {System.out.println(re);}

			AfficheInventaire();
			//Verifie si fini
			boolean fin1 = FinParti();
			if(fin1){
				try{
					obs.PartieFini(this.name);
				}
				catch (RemoteException re) { System.out.println(re) ; }
				try{
					Thread.sleep(100000);
				}catch(InterruptedException e){System.out.println(e);}
			}

			break;


		//Prends seulement si le producteur poss�de au moins une demande "entiere" de ressources
		case Cooperatif :

			//Change pour observer le producteur de la ressource
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
						if(this.ListProducteur.get(indexProd2).GetRessources().getExemplaires() > this.ListProducteur.get(indexProd2).GetCanGive())
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

			AfficheInventaire();

			//Verifie si fini
			boolean fin2 = FinParti();
			if(fin2){
				try{
					obs.PartieFini(this.name);
					try{
						Thread.sleep(10000);
					}catch(InterruptedException e){System.out.println(e);}
				}
				catch (RemoteException re) { System.out.println(re) ; }
			}

			//Poke l'observateur de ses action et son etat

			break;


		//Ne fait que voler aux autres joueurs
		case Voleur :

			//Change pour observer le producteur de la ressource


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
				//Recup�re les stocks des joueurs
				ArrayList<Ressources> tmp = null;
				try {tmp = Observation(this.ListJoueur.get(j));}
				catch (RemoteException re) {System.out.println(re);}

				if(tmp != null)
				{
					for(int k =0; k<tmp.size();k++)
					{
						//Regarde si le joueurs poss�de la ressource , en plus grand nombre que le pr�c�dent
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
			AfficheInventaire();

			//Verifie si fini
			boolean fin3 = FinParti();
			if(fin3){
				try{
					obs.PartieFini(this.name);
					try{
						Thread.sleep(10000);
					}catch(InterruptedException e){System.out.println(e);}
				}
				catch (RemoteException re) { System.out.println(re) ; }
			}
			//Poke l'observateur de ses action et son etat

			break;

		/*Personnalite a rajouter*/

		default:
			break;
		}
		//ThreadSleep ?
	}

	public void run() {
		while(true && !this.finParti)
		{
			tourDeJeu();
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


	public boolean DemandeRessource(ClientInterface p) throws RemoteException
	{
		////Verifie que le joueur est bien en mode demande pour demander
		if(GetMode() != Mode.Demande)
			return false;

		Ressources NewRessource = p.GetRessources();
		int nbr = p.PrendreRessource();

		//Poke de l'observateur pour lui dire que le joueur demande a Producteur
		obs.generationLog(this.name, this.monType, Action.Demande, NewRessource, nbr, p.getName(), p.getmonType());

		return this.AjoutStock(NewRessource , nbr);
	}


	//Donne le nombre pouvant etre vole
	synchronized public int VolRessourceVictime(Ressources r)
	{
		//Si le joueur est en mode protection marche pas
		if(GetMode() == Mode.Observation)
		{
			return 0;
		}
			

		int index = SearchRessource(r);

		if(index == -1)
			return -1;
		else
		{
			int tmp = 	StockRessources.get(index).getExemplaires();
			StockRessources.get(index).takeRessources(tmp);
			return tmp;
		}
	}


	//Joueur Voleur regarde si il peut voler
	synchronized public boolean VolRessourceAgresseur(ClientInterface j, Ressources r ) throws RemoteException
	{
		//Verifie que le voleur est bien en mode vol
		if(GetMode() != Mode.Vol)
			return false;

		int StockPris = j.VolRessourceVictime(r);

		//Verifie si c'est BullShit la ressource ou si y'a pas
		if(StockPris == 0)
		{
			obs.generationLog(j.getName(), j.getmonType(), Action.Punition, this.getName(), this.getmonType());
			//Punition
			return false;
		}
		else if(StockPris == -1)
			return false;
		else{
			obs.generationLog(this.name, this.monType, Action.Vol, r, StockPris, j.getName(), j.getmonType());
			return this.AjoutStock(r, StockPris);
		}
			
	}

	public void AskAction()
	{
		Scanner sc = new Scanner(System.in);
		int action = 0;
		System.out.println("Plusieurs Operations sont disponibles, veuillez tapper le Numero de l'action : \n");
		System.out.println("1:Demande Ressource \t 2:Vol Ressource \t 3:Mode Observation \t 4:Afficher Inventaire\n ");
		
		do{action = sc.nextInt();}
		while(action != 1 || action != 2 || action != 3 );
		
		switch(action)
		{
		case 1 : 
			System.out.println("A quelle producteur voulez vous prendre les ressources ?");
			for(int i = 0 ; i < this.ListProducteur.size() ; i++)
			{
				
			}
			
			
			break;
		
		case 2 : System.out.println("A quelle joueur voulez vous prendre les ressources ?");
			break;
		
			
			
		case 3 : System.out.println("Passage en mode Observation , vous pouvez punir les joueurs tentant de vous voler \n");
			this.mode = Mode.Observation;
			break;
		
		case 4 : 
			AfficheInventaire();
			AskAction();
			break;
		default :
			break;
		}
		
	}

	//Renvoie la liste des ressources du joueur observ�
	synchronized public ArrayList<Ressources> Observation(ClientInterface j) throws RemoteException
	{
		return j.GetStock();
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
			try {
				if(this.ListProducteur.get(i).GetRessources().equals(r) )
				{
					if(this.ListProducteur.get(i).GetRessources().getExemplaires() > 0)
						return i;
					else index = i;
				}
			}
			catch (RemoteException re) {System.out.println(re) ;}
		}
		return index;
	}


	synchronized private boolean FinParti()
	{
		for(int i =0 ; i<StockRessources.size() ; i++)
		{
			if(StockRessources.get(i).getExemplaires() < objectif)
			{
				return false;
			}
		}
		return true;
	}

	synchronized private void AfficheInventaire(){
		for(int i=0;i<this.StockRessources.size();i++){
			System.out.println(this.StockRessources.get(i).getName()+" : "+this.StockRessources.get(i).getExemplaires());
		}
	}
}
