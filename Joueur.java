import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;


public class Joueur extends Client{

	static final long serialVersionUID = 42;

	//Personnalit� du joueurs
	private Personnalite perso;

	//Liste de ressources du joueurs
	private ArrayList<Ressources> StockRessources = new ArrayList<Ressources>();

	//true si le joueur est reel
	private boolean isJoueurIRL;

	//Mode actuel du joueurs
	private Mode mode = Mode.Demande;

	//Nombre d'exemplaire que doit atteindre chaque ressources de la liste StockRessources
	private int objectif;

	//True si c'est en mode tour par tour
	private boolean tourParTour;

	//Variable permettant de savoir si le joueur est actuellement puni
	private boolean Ispunit = false ;

	//Constructeur
	public Joueur(String name,String ServerName,String NumPort,String NomServise, Personnalite perso, boolean isIRL , int objectif,boolean tourParTour ) throws RemoteException{
		super(name,ServerName,NumPort,NomServise);

		this.perso = perso;
		this.isJoueurIRL = isIRL;
		this.objectif = objectif;
		this.monType = Type.Joueur;
		this.tourParTour = tourParTour;
	}


	/** Debute l'Agent Joueur en initialisant sa liste de ressources
	 * @return void
	 */
	public void startAgent(){
		System.out.println("Start");

		//Cree chaque ressource et l'initialise a 0
		for(int i = 0; i< ListProducteur.size() ; i++)
		{
			try {
				if(SearchRessource(ListProducteur.get(i).GetRessources())==-1)
					StockRessources.add(new Ressources(ListProducteur.get(i).GetRessources().getName(), 0));
			}
			catch (RemoteException re) { System.out.println(re) ; }
		}
		System.out.println("Fin Init");
		StartTimer = System.currentTimeMillis();
		if(!tourParTour){
			Thread t = new Thread(this);
			t.start();
		}
	}


	/** Fonction dictant le tour de jeu du joueur suivant sa personnalite
	 * @return void
	 */
	public void tourDeJeu(){

		//Si le joueur est reel alors demande au joueur son action par la fonction AskAction()
		if(isJoueurIRL){
			try{
				AskAction();
			}catch(RemoteException re) { System.out.println(re) ; }
			return;
		}

		//Si le joueur est puni alors passe son tour
		if(Ispunit && tourParTour)
		{
			return;
		}
		else if(Ispunit && !tourParTour)
		{
			try{
				//Fait dormir le joueur 1 seconde si le mode de jeu est en auto
			      Thread.sleep(1000);
			    }catch(InterruptedException e){System.out.println(e);}
		}

		//Recupere la personnalite du joueur
		Personnalite perso = this.perso;


		//Choisit l'action suivant la personnalite
		switch(perso)
		{

		//COMPORTEMENT : Prends pour chaque ressources n'ayant pas atteint l'objectif le nombre a avoir pour finir dans le 1er producteur
		case Individuel :

			//Change le mode si besoin en demande
			if(GetMode() != Mode.Demande)
				SetMode(Mode.Demande);

			//Boucle qui regarde les ressources qui n'ont pas atteint l'objectif
			int indexRessource1 = -1 , indexProd1 = -1 ;
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
							//Si pas 0 alors il prend ce producteur
							break;
						}
					}
					catch (RemoteException re) {System.out.println(re);}
				}
			}

			SetMode(Mode.Demande);
			try {
				//Verifie que quelqu'un n'a pas pris entre temps
				if(this.ListProducteur.get(indexProd1).GetRessources().getExemplaires() != 0){
					DemandeRessource(this.ListProducteur.get(indexProd1));
				}
				else
					SetMode(Mode.Observation);
			} catch (RemoteException re) {System.out.println(re);}


			AfficheInventaire();

			//Verifie si le joueur a fini la partie
			boolean fin1 = FinParti();
			if(fin1){
				try{
					obs.PartieFini(this.name);
				}
				catch (RemoteException re) { System.out.println(re) ; }
				return;
			}

			break;


		//COMPORTEMENT : Prends seulement si le producteur possede au moins une demande "entiere" de ressources
		case Cooperatif :

			int indexProd2 = -1;

			//Trouve les ressources qui n'ont pas atteint l'objectif
			for(int i = 0; i<StockRessources.size() ; i++)
			{
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

			SetMode(Mode.Demande);
			try {
				//Verifie que quelqu'un n'a pas pris entre temps
				if(this.ListProducteur.get(indexProd2).GetRessources().getExemplaires() != 0){
					DemandeRessource(this.ListProducteur.get(indexProd2));
				}
				else
					SetMode(Mode.Observation);
			} catch (RemoteException re) {System.out.println(re);}

			AfficheInventaire();

			//Verifie si le joueur a fini la partie
			boolean fin2 = FinParti();
			if(fin2){
				try{
					obs.PartieFini(this.name);
					return;
				}
				catch (RemoteException re) { System.out.println(re) ; }
			}

			break;


		//COMPORTEMENT : Vol les ressources manquantes aux different joueur
		case Voleur :

			int indexJoueur = 0 , indexRessource2 = -1;

			//Trouve la ressources qui n'ont pas atteint l'objectif
			for(int i = 0; i<StockRessources.size() ; i++)
			{
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
				//Recupere les stocks du joueur j
				ArrayList<Ressources> tmpJoueur = null;
				try {tmpJoueur = Observation(this.ListJoueur.get(j));}
				catch (RemoteException re) {System.out.println(re);}


				if(tmpJoueur != null)
				{
					for(int k =0; k<tmpJoueur.size();k++)
					{
						//Regarde si le joueurs possede la ressource , en plus grand nombre que le precedent
						if(tmpJoueur.get(k).equals(this.StockRessources.get(indexRessource2)) && tmpJoueur.get(k).getExemplaires() > max)
						{
							max = tmpJoueur.get(k).getExemplaires();
							//Le joueur actuel est le plus interressent a voler
							indexJoueur = j;
						}
					}
				}
			}

			SetMode(Mode.Vol);

			try {VolRessourceAgresseur(this.ListJoueur.get(indexJoueur), StockRessources.get(indexRessource2));}
			catch (RemoteException re) {System.out.println(re);}
			AfficheInventaire();

			//Verifie si le joueur a fini la partie
			boolean fin3 = FinParti();
			if(fin3){
				try{
					obs.PartieFini(this.name);
					return;
				}
				catch (RemoteException re) { System.out.println(re) ; }
			}

			break;

		//COMPORTEMENT : Plus il est pret de la victoire plus il a de chance de se mettre en  protection pour eviter de se faire voler
		case Mefiant :

			//Recupere le nombre de ressource ayant atteint l'objectif
			int nbrFini=0;
			for(int i = 0 ; i<StockRessources.size() ; i++)
			{
				if(StockRessources.get(i).getExemplaires() == this.objectif)
				{
					nbrFini++;
				}
			}

			//Si le nombre entre 0 et le nombre de ressources fini est Superieur au nbr des ressources de la liste/2 alors le joueur se met en Observation
			if(Math.random()*nbrFini > StockRessources.size()/2)
			{
				SetMode(Mode.Observation);
				return;
			}
			else
			{
				//Cherche le producteur le plus rentable , c'est a dire celui qui poss�de le plus de ressource parmi les ressources dont le joueur a besoin
				int indexProdRentable = 0;
				int MaxProdRentable = 0;
				for(int i = 0 ; i<ListProducteur.size();i++)
				{
					int indexR = 0;
					try {
						indexR = SearchRessource(ListProducteur.get(i).GetRessources());
					} catch (RemoteException re) {System.out.println(re);}

					//Si la ressource du producteur na pas atteint l'objectif alors ca choisit celle ci
					if(StockRessources.get(indexR).getExemplaires() < this.objectif)
					{
						try {
							if(ListProducteur.get(i).GetRessources().getExemplaires() > MaxProdRentable)
							{
								MaxProdRentable = ListProducteur.get(i).GetRessources().getExemplaires();
								indexProdRentable = i;
							}
						} catch (RemoteException re) {System.out.println(re);}
					}
				}

				SetMode(Mode.Demande);

				try {
					//Verifie que quelqu'un n'a pas pris entre temps
					if(this.ListProducteur.get(indexProdRentable).GetRessources().getExemplaires() != 0){
						DemandeRessource(this.ListProducteur.get(indexProdRentable));
					}
					else
						SetMode(Mode.Observation);
				} catch (RemoteException re) {System.out.println(re);}

				AfficheInventaire();

				//Verifie si le joueur a fini la partie
				boolean fin4 = FinParti();
				if(fin4){
					try{
						obs.PartieFini(this.name);
						return;
					}
					catch (RemoteException re) { System.out.println(re) ; }
				}

			}

		break;

		//COMPORTEMENT : Regarde chez tout les producteurs pour voir qui poss�de le plus de ressource n'ayant pas atteint l'objectif en pr�viligiant les ressources dont il a le plus besoin
		case Stratege :

			/*
			int MaxRessourcePro =  0 , IndexMaxProd = 0 , Differenceobj = 0;
			for(int i=0 ; i < this.ListProducteur.size() ; i++)
			{
				int indexR = 0;
				try {
					indexR = SearchRessource(ListProducteur.get(i).GetRessources());
				} catch (RemoteException re) {System.out.println(re);}

				if(StockRessources.get(indexR).getExemplaires() < this.objectif)
				{
					try {
					int tmpDifferenceobj =  this.objectif - this.StockRessources.get(indexR).getExemplaires();
					if(tmpDifferenceobj > Differenceobj)
					//Si la difference entre l'objectif et les exemplaires est plus importante dans cette ressource alors choisit celle ci
						if(tmpDifference < MaxRessourcePro)
						{
							MaxRessourcePro = tmpDifference;
							IndexMaxProd = i;
						}
					} catch (RemoteException re) {System.out.println(re);}
				}

			}

			SetMode(Mode.Demande);

			try {
				//Verifie que quelqu'un n'a pas pris entre temps
				if(this.ListProducteur.get(IndexMaxProd).GetRessources().getExemplaires() != 0){
					DemandeRessource(this.ListProducteur.get(IndexMaxProd));
				}
				else
					SetMode(Mode.Observation);
			} catch (RemoteException re) {System.out.println(re);}

			AfficheInventaire();

			//Verifie si le joueur a fini la partie
			boolean fin5 = FinParti();
			if(fin5){
				try{
					obs.PartieFini(this.name);
					return;
				}
				catch (RemoteException re) { System.out.println(re) ; }
			}*/

			break;

		default:
			break;
		}
	}

	/** Fonction qui permet au joueur de jouer en automatique
	 * return void
	 */
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

	public int Getobjectif()
	{
		return this.objectif;
	}

	/** Demande et prend le nombre de ressource que lui donne le producteur
	 * @param p 	InterfaceClient mais etant un producteur
	 * @return boolean	suivant si cela a echoue ou non
	 */
	public boolean DemandeRessource(ClientInterface p) throws RemoteException
	{
		////Verifie que le joueur est bien en mode demande pour demander
		if(GetMode() != Mode.Demande)
			return false;

		Ressources NewRessource = p.GetRessources();
		int nbr = p.PrendreRessource();

		if(nbr != 0)
		{
		//Poke de l'observateur pour lui dire que le joueur demande a Producteur
		//obs.generationLog(this.name, this.monType, Action.Demande, NewRessource, nbr, p.getName(), p.getmonType());
		this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,this.monType+"  "+this.name+" Prend "+NewRessource.getName()+"  "+nbr+"  "+p.getmonType()+"  "+p.getName()));
		System.out.println(this.monType+"  "+this.name+" Prend "+NewRessource.getName()+"  "+nbr+"  "+p.getmonType()+"  "+p.getName());
		}

		//Ajoute le nombre que le producteur lui a donne
		return this.AjoutStock(NewRessource , nbr);
	}


	/** Si en mode protection renvoit 0 , sinon le nombre de ressource
	 * @param r ressource demande
	 * @return int
	 */
	synchronized public int VolRessourceVictime(Ressources r){
		//Si le joueur est en mode observation, marche pas
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


	/** Si la demande de vol retourne 0 alors punit le joueur voleur sinon vole la victime
	 * @param j Joueur a voler
	 * @param r ressource a voler
	 * @return boolean  suivant si ca marche ou pas
	 */
	synchronized public boolean VolRessourceAgresseur(ClientInterface j, Ressources r ) throws RemoteException{
		//Verifie que le voleur est bien en mode vol
		if(GetMode() != Mode.Vol)
			return false;

		int StockPris = j.VolRessourceVictime(r);

		//Punit le joueur
		if(StockPris == 0)
		{
			//Prend la moiti� de la plus grande ressource et Le punit d'un tour
			int indexR = 0;
			for(int i = 0 ; i<StockRessources.size() ; i++)
			{
				if(StockRessources.get(indexR).getExemplaires() >= indexR)
				{
					indexR = i;
				}
			}
			int punition = StockRessources.get(indexR).getExemplaires()/2;
			StockRessources.get(indexR).takeRessources(punition);
			Ispunit = true;

			this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,j.getmonType()+"  "+j.getName()+" Punit "+r.getName()+"  "+punition+"  "+this.getmonType()+"  "+this.getName()));
			return false;
		}
		else if(StockPris == -1)
			return false;
		else{
			//Poke l'observateur de l'action
			this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,this.monType+"  "+this.name+" Vol "+r.getName()+"  "+StockPris+"  "+j.getmonType()+"  "+j.getName()));

			return this.AjoutStock(r, StockPris);
		}

	}


	/** Demande au joueur reel l'action a effectuer
	 * @return void
	 */
	public void AskAction() throws RemoteException{
		//Menu de l'utilisateur
		System.out.println("Plusieurs Operations sont disponibles, veuillez tapper le Numero de l'action :");
		System.out.println("1:Demande Ressource \t 2:Vol Ressource \t 3:Mode Observation \t 4:Afficher Inventaire ");

		Scanner sc = new Scanner(System.in);
		int action = sc.nextInt();
		System.out.println(action);

		//Ajout pour voir si le double input est regl�
		sc.close();

		//Regarde l'action demander
		switch(action)
		{

		//Demande a un producteur
		case 1 :
			int action1 = -1;

			//Choisit le producteur
			System.out.println("A quelle producteur voulez vous prendre les ressources ?");
			for(int i = 0 ; i < this.ListProducteur.size() ; i++)
			{
				System.out.println(i+":"+this.ListProducteur.get(i).getName()+","+this.ListProducteur.get(i).GetRessources().getName());
			}
			System.out.println("-1 : Retour Menu Choix");
			Scanner sc1 = new Scanner(System.in);
			action1 = sc1.nextInt();

			if(action1 == -1)
			{
				AskAction();
				return;
			}
			//Ajout pour voir si le double input est regl�
			sc1.close();

			if(GetMode() != Mode.Demande)
				SetMode(Mode.Demande);

			DemandeRessource(this.ListProducteur.get(action1));
			System.out.println("Prise faite");
			break;

		//Vol a un joueur
		case 2 :
			int action2 =-1, action3 =-1;

			//Choisit le joueur
			System.out.println("A quelle joueur voulez vous prendre les ressources ?");
			for(int i = 0 ; i < this.ListJoueur.size() ; i++)
			{
				System.out.println(i+":"+this.ListJoueur.get(i).getName()+"\n");
				for(int j =0; j< this.ListJoueur.get(i).GetStock().size() ; j++)
				{
					System.out.println("\t"+j+":"+this.ListJoueur.get(i).GetStock().get(j).getName()+","+this.ListJoueur.get(i).GetStock().get(j).getExemplaires());
				}
			}
			System.out.println("-1 : Retour Menu Choix");
			if(action2 == -1)
			{
				AskAction();
				return;
			}

			Scanner sc2 = new Scanner(System.in);
			action2 = sc2.nextInt();

			//Ajout pour voir si le double input est regl�
			sc2.close();

			//Choisit la ressource
			System.out.println("Et quelle ressources voulez vous prendres ?");
			Scanner sc3 = new Scanner(System.in);
			action3 = sc3.nextInt();
			sc3.close();


			SetMode(Mode.Vol);
			VolRessourceAgresseur(this.ListJoueur.get(action2), this.ListJoueur.get(action2).GetStock().get(action3));
			System.out.println("Vol fait");

			break;


		//Passe en mode observation
		case 3 : System.out.println("Passage en mode Observation , vous pouvez punir les joueurs tentant de vous voler \n");
			this.mode = Mode.Observation;
			break;

		//Affiche l'inventaire du joueur et redemande une action
		case 4 :
			AfficheInventaire();
			AskAction();
			break;
		default :
			break;
		}

	}

	/** Retourne la liste de ressource du joueur
	 * @param j le joueur observe
	 * @return Liste de ressource
	 * @throws RemoteException
	 */
	synchronized public ArrayList<Ressources> Observation(ClientInterface j) throws RemoteException
	{
		return j.GetStock();
	}



	/** Ajout au stock de ressource le nombre donne
	 * @param r Ressource a ajouter
	 * @param nbr le nombre a ajouter
	 * @return boolean suivant si cela a reussit
	 */
	private boolean AjoutStock(Ressources r, int nbr){
		int indexStock = SearchRessource(r);
		if(indexStock == -1)
		{
			Ressources r1 = new Ressources(r.getName(),nbr);
			StockRessources.add(r1);
		}
		else
		{
			Ressources r2 = StockRessources.get(indexStock);
			r2.addRessources(nbr);
			StockRessources.set(indexStock, r2);
		}

		return true;
	}


	/** Recherche la ressource dans le stock du joueur
	 * @param r Ressource a chercher
	 * @return int l'index dans la liste de la ressource rechercher
	 */
	private int SearchRessource(Ressources r){
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


	/** Recherche le producteur produisant la ressource
	 * @param r Ressource chercher chez les producteur
	 * @return int l'index dans la liste de producteur du producteur trouver
	 */
	private int SearchProducteur(Ressources r){
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


	/** Verifie si le joueur a fini la parti en checkant toute les ressources
	 * @return boolean , true si fini false sinon
	 */
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

	/** Affiche l'inventaire du joueur
	 * @return void
	 */
	synchronized private void AfficheInventaire(){
		for(int i=0;i<this.StockRessources.size();i++){
			System.out.println(this.StockRessources.get(i).getName()+" : "+this.StockRessources.get(i).getExemplaires());
		}
	}
}
