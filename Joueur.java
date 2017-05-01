import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.Scanner;

/**@author WENDLING Quentin URBAIN Nathan*/

public class Joueur extends Client{

	static final long serialVersionUID = 42;

	//Personnalite du joueur
	private Personnalite perso;

	//Liste de ressources du joueurs
	private ArrayList<Ressources> StockRessources = new ArrayList<Ressources>();

	//True si le joueur est reel
	private boolean isJoueurIRL;

	//Mode actuel du joueur
	private Mode mode = Mode.Demande;

	//Nombre d'exemplaire que doit atteindre chaque ressource de la liste StockRessources
	private int objectif;

	//True si c'est en la partie est en tour par tour
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


	/** Debute l'Agent Joueur en initialisant sa liste de ressources a 0
	 * @return void
	 */
	public void startAgent(){
		System.out.println("Start");

		for(int i = 0; i< ListProducteur.size() ; i++)
		{
			try {
				//Si la ressource de ce producteur n'existe pas chez le joueur alors cree la ressource dans la liste
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

		//Si le joueur est reel alors demande au joueur son action de tour
		if(isJoueurIRL){
			try{
				AskAction();
			}catch(RemoteException re) { System.out.println(re) ; }
			return;
		}

		//Si le joueur est puni alors passe son tour, en tour par tour
		if(Ispunit && tourParTour)
		{
			return;
		}
		else if(Ispunit && !tourParTour)
		{
			try{
				//Fait dormir le joueur 1 seconde si le mode de jeu est en auto
				System.out.println("Je suis punis ='(");
			      Thread.sleep(2000);
			    }catch(InterruptedException e){System.out.println(e);}
		}

		//Recupere la personnalite du joueur
		Personnalite perso = this.perso;


		//Choisit l'action suivant la personnalite
		switch(perso)
		{

		//COMPORTEMENT : Demande a un producteur ayant de la ressource la 1er ressource du stock n'ayant pas atteint l'objectif
		case Individuel :

			//Boucle qui regarde les ressources qui n'ont pas atteint l'objectif
			int indexRessource1 = -1 , indexProd1 = -1 ;
			for(int i = 0; i<StockRessources.size() ; i++)
			{
				if(StockRessources.get(i).getExemplaires() < objectif)
				{
					indexRessource1 = i;

					//Recupere le producteur de la ressources manquante
					indexProd1 = SearchProducteur(StockRessources.get(indexRessource1));

					//Verifie qu'il n'a pas 0 ressource chez le producteur en question
					try {
						if(this.ListProducteur.get(indexProd1).GetRessources().getExemplaires() > 0)
						{
							//Si pas 0 alors il selectionne le producteur
							break;
						}
					}
					catch (RemoteException re) {System.out.println(re);}
				}
			}

			//Passe en mode demande
			SetMode(Mode.Demande);
			try {
				//Verifie que quelqu'un n'a pas pris entre temps
				if(this.ListProducteur.get(indexProd1).GetRessources().getExemplaires() != 0){
					DemandeRessource(this.ListProducteur.get(indexProd1));
				}
				//Si le producteur de possede plus de ressource alors se remet en observateur
				else
					SetMode(Mode.Observation);
			} catch (RemoteException re) {System.out.println(re);}


			AfficheInventaire();

			//Verifie si le joueur a fini la partie
			boolean fin1 = FinParti();
			if(fin1){
				try{
					//Si la partie est fini alors le signale a l'observateur
					obs.PartieFini(this.name);
				}
				catch (RemoteException re) { System.out.println(re) ; }
				return;
			}

			break;


		//COMPORTEMENT : Prend au producteur de la ressource n'ayant pas atteint l'objectif si il possede au moins une demande "entiere" de ressource en question
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

			//Pass en mode demande car a selectionne le producteur
			SetMode(Mode.Demande);
			try {
				//Verifie que quelqu'un n'a pas pris entre temps et que le producteur encore possede assez
				if(this.ListProducteur.get(indexProd2).GetRessources().getExemplaires() > this.ListProducteur.get(indexProd2).GetCanGive()){
					DemandeRessource(this.ListProducteur.get(indexProd2));
				}
				else
				//Si le producteur de possede pas assez de ressource alors se remet en observateur
					SetMode(Mode.Observation);
			} catch (RemoteException re) {System.out.println(re);}

			AfficheInventaire();

			//Verifie si le joueur a fini la partie
			boolean fin2 = FinParti();
			if(fin2){
				try{
					//Si la partie est fini alors le signale a l'observateur
					obs.PartieFini(this.name);
					return;
				}
				catch (RemoteException re) { System.out.println(re) ; }
			}

			break;


		//COMPORTEMENT : Vol les ressources manquantes aux differents joueurs
		case Voleur :

			int indexJoueur = 0 , indexRessource2 = -1;

			//Trouve la 1er ressource qui n'a pas atteint l'objectif
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
				//Recupere le stock des ressources du joueur j dans tmpJoueur
				ArrayList<Ressources> tmpJoueur = null;
				tmpJoueur = Observation(this.ListJoueur.get(j));


				//Si sont stock n'est pas null alors cherche la ressource voulu
				if(tmpJoueur != null)
				{
					for(int k =0; k<tmpJoueur.size();k++)
					{
						//Regarde si le joueurs possede la ressource , et en plus grosse quantite que le precedent
						if(tmpJoueur.get(k).equals(this.StockRessources.get(indexRessource2)) && (tmpJoueur.get(k).getExemplaires() > max))
						{
							max = tmpJoueur.get(k).getExemplaires();
							System.out.println(max);

							//Le joueur actuel est le plus interressent a voler
							indexJoueur = j;
						}
					}
				}
			}

			//Si rien a voler alors prend chez les producteurs
			if(max == 0)
			{
				System.out.println("La je dois prendre");
				int MaxR = 0;
				int indexPM = 0;
				for(int i = 0 ; i<this.ListProducteur.size() ; i++)
				{
					try {
						if( this.ListProducteur.get(i).GetRessources().equals(this.StockRessources.get(indexRessource2)) && this.ListProducteur.get(i).GetRessources().getExemplaires() > MaxR)
						{
							indexPM = i;
							MaxR = this.ListProducteur.get(i).GetRessources().getExemplaires();
						}
					}catch (RemoteException re) {System.out.println(re);}
				}

				//Passe en mode demande
				SetMode(Mode.Demande);

				try {
					//Verifie que quelqu'un n'a pas pris entre temps
					if(this.ListProducteur.get(indexPM).GetRessources().getExemplaires() != 0){
						DemandeRessource(this.ListProducteur.get(indexPM));
					}
					//Si le producteur de possede plus de ressource alors se remet en observateur
					else
						SetMode(Mode.Observation);
				} catch (RemoteException re) {System.out.println(re);}

			}
			else
			{
				//Passe en mode vol pour voler le joueur
				SetMode(Mode.Vol);

			  VolRessourceAgresseur(this.ListJoueur.get(indexJoueur), StockRessources.get(indexRessource2));

			}

			AfficheInventaire();

			//Verifie si le joueur a fini la partie
			boolean fin3 = FinParti();
			if(fin3){
				try{
					//Si la partie est fini alors le signale a l'observateur
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

			//Si le nombre entre 0 et le nombre de ressources fini est Superieur au nombre des ressources de la liste/2 alors le joueur se met en Observation
			if(Math.random()*nbrFini > StockRessources.size()/2)
			{
				SetMode(Mode.Observation);
				return;
			}
			else
			{
				//Cherche le producteur le plus rentable , c'est a dire celui qui possede le plus de ressource parmi les ressources dont le joueur � besoin
				int indexProdRentable = 0;
				int MaxProdRentable = 0;
				for(int i = 0 ; i<ListProducteur.size();i++)
				{
					int indexR = 0;
					try {
						indexR = SearchRessource(ListProducteur.get(i).GetRessources());
					} catch (RemoteException re) {System.out.println(re);}

					//Si la ressource du producteur n'a pas atteint l'objectif alors ca choisit celle ci
					if(StockRessources.get(indexR).getExemplaires() < this.objectif)
					{
						try {
							//Si Le producteut possede plus que le precedent alors on sauvgarde celui ci
							if(ListProducteur.get(i).GetRessources().getExemplaires() > MaxProdRentable)
							{
								MaxProdRentable = ListProducteur.get(i).GetRessources().getExemplaires();
								indexProdRentable = i;
							}
						} catch (RemoteException re) {System.out.println(re);}
					}
				}

				//Passe en mode demande
				SetMode(Mode.Demande);

				try {
					//Verifie que quelqu'un n'a pas pris entre temps
					if(this.ListProducteur.get(indexProdRentable).GetRessources().getExemplaires() != 0){
						DemandeRessource(this.ListProducteur.get(indexProdRentable));
					}
					else
					//Si le producteur n'a plus de ressource alors passe en observation
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

		//COMPORTEMENT : Regarde chez tout les producteurs pour voir qui possede le plus de ressource n'ayant pas atteint l'objectif en previligiant les ressources dont il a le plus besoin
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

	//Fonction qui permet au joueur de jouer en automatique
	public void run() {
		while(true && !this.finParti)
		{
			tourDeJeu();
		}
	}


	//Fonction qui retourne le stock de ressource
	synchronized public ArrayList<Ressources> GetStock()
	{
		return this.StockRessources;
	}


	//Fonction qui renvoi la personnalite
	public Personnalite GetPersonnalite()
	{
		return this.perso;
	}

	//Fonction qui renvoi le mode actuel
	synchronized public Mode GetMode()
	{
		return this.mode;
	}

	//Fonction qui renvoi true si le joueur est reel
	public boolean GetisJoueurIRL()
	{
		return this.isJoueurIRL;
	}

	//Fonction qui change le mode du joueur
	public void SetMode(Mode m)
	{
		this.mode = m;
	}

	//Fonction qui recupere l'objectif a atteindre
	public int Getobjectif()
	{
		return this.objectif;
	}


	/** Demande et prend le nombre de ressource que peut lui donner le producteur
	 * @param p InterfaceClient, une producteur
	 * @return boolean suivant si cela a echoue ou non
	 */
	public boolean DemandeRessource(ClientInterface p) throws RemoteException
	{
		////Verifie que le joueur est bien en mode demande pour demander
		if(GetMode() != Mode.Demande)
			return false;

		//Recupere les infos de la ressources pour en la cree
		Ressources NewRessource = p.GetRessources();

		//Le nombre que peut donne le producteur
		int nbr = p.PrendreRessource();

		if(nbr != 0)
		{
		//Ajoute le log detaillant la prise de ressource au producteur
		this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,this.monType+"  "+this.name+" Prend "+NewRessource.getName()+"  "+nbr+"  "+p.getmonType()+"  "+p.getName()));
		System.out.println(this.monType+"  "+this.name+" Prend "+NewRessource.getName()+"  "+nbr+"  "+p.getmonType()+"  "+p.getName());
		}

		//Ajoute le nombre que le producteur lui a donne
		return this.AjoutStock(NewRessource , nbr);
	}


	/** Fonction qui Renvoie le nombre d'exemplaire de la ressource r prise
	 * @param r ressource voulant etre vole
	 * @return int le nombre de ressource vole , ou -2 si le joueur �tait en Observation
	 */
	synchronized public int VolRessourceVictime(Ressources r){
		//Si le joueur est en mode observation, renvoie 0 , aucun vol
		if(GetMode() == Mode.Observation)
		{
			return -2;
		}


		int index = SearchRessource(r);

		//Si la ressource n'existe pas alors renvoie -1
		if(index == -1)
			return -1;
		else
		{
			//Renvoie le nombre de ressource vole et on soustrait ce nombre au stock actuel
			int tmp = 	StockRessources.get(index).getExemplaires() /2 ;
			StockRessources.get(index).takeRessources(tmp);
			return tmp;
		}
	}


	/** Fonction permettant de voler une ressource d'un joueur
	 * @param j Joueur a voler
	 * @param r ressource a voler
	 * @return boolean  suivant si ca marche ou pas
	 */
	synchronized public boolean VolRessourceAgresseur(ClientInterface j, Ressources r ){
		//Verifie que le voleur est bien en mode vol
		if(GetMode() != Mode.Vol)
			return false;

		int StockPris = -2;
		try{
			//Regarde combien on a prit au joueur
			StockPris = j.VolRessourceVictime(r);
		}catch (RemoteException re) { System.out.println(re) ; }


		//Si le nombre est -2 alors on Punit le joueur
		if(StockPris == -2)
		{
			//Prend la moitie de la plus grande ressource et Le punit d'un tour
			int indexR = 0;
			int MaxR = 0;
			for(int i = 0 ; i<StockRessources.size() ; i++)
			{
				if(StockRessources.get(i).getExemplaires() >= MaxR)
				{
					MaxR = StockRessources.get(i).getExemplaires();
					indexR = i;
				}
			}
			int punition = StockRessources.get(indexR).getExemplaires()/2;
			if(punition < 0)
			{
				punition = 0;
			}			
			//Prend la moitie de la ressource
			StockRessources.get(indexR).takeRessources(punition);

			//Actuellement punits
			Ispunit = true;
			try{
				if(punition > 0)
					//Ajoute le log detaillant la punition
					this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,j.getmonType()+"  "+j.getName()+" Punit "+r.getName()+"  "+punition+"  "+this.getmonType()+"  "+this.getName()));
					//Ajoute le log detaillant le gain de ressource pour le vole
					punition = punition/2;					
					j.DonnerAmende(r, punition);
			}catch (RemoteException re) { System.out.println(re) ; }
			return false;
		}
		//Si -1 alors la ressource n'existait pas chez le joueur
		else if(StockPris == -1)
			return false;
		else{
			try{
				//Ajoute le log detaillant le vol de ressource au joueur
				this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,this.monType+"  "+this.name+" Vol "+r.getName()+"  "+StockPris+"  "+j.getmonType()+"  "+j.getName()));
			}catch (RemoteException re) { System.out.println(re) ; }
			//Ajoute le nombre vole au stock du joueur voleur
			return this.AjoutStock(r, StockPris);
		}

	}

	public void DonnerAmende(Ressources r,int amende){
		this.AjoutStock(r,amende);
		return;
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

		sc.close();

		//Regarde l'action demander
		switch(action)
		{

		//Demande a un producteur
		case 1 :
			int action1 = -1;

			SetMode(Mode.Observation);

			//Affiche les producteurs et leur stock
			System.out.println("A quelle producteur voulez vous prendre les ressources ?");
			for(int i = 0 ; i < this.ListProducteur.size() ; i++)
			{
				System.out.println(i+":"+this.ListProducteur.get(i).getName()+","+this.ListProducteur.get(i).GetRessources().getName());
			}
			//Choix de au menu principale
			System.out.println("-1 : Retour Menu Choix");

			//Recupere le producteur
			Scanner sc1 = new Scanner(System.in);
			action1 = sc1.nextInt();

			//Si le choix -1 est fait alors on revient au menu
			if(action1 == -1)
			{
				AskAction();
				return;
			}
			sc1.close();

			//Pass en mode demande
			if(GetMode() != Mode.Demande)
				SetMode(Mode.Demande);

			//Prend la ressource
			DemandeRessource(this.ListProducteur.get(action1));
			System.out.println("Prise faite");
			break;

		//Vol a un joueur
		case 2 :
			int action2 =-1, action3 =-1;

			//Affiche tout les joueurs et leur inventaire respectifs
			System.out.println("A quelle joueur voulez vous prendre les ressources ?");
			for(int i = 0 ; i < this.ListJoueur.size() ; i++)
			{
				System.out.println(i+":"+this.ListJoueur.get(i).getName()+"\n");
				for(int j =0; j< this.ListJoueur.get(i).GetStock().size() ; j++)
				{
					System.out.println("\t"+j+":"+this.ListJoueur.get(i).GetStock().get(j).getName()+","+this.ListJoueur.get(i).GetStock().get(j).getExemplaires());
				}
			}
			//Choix de Retour au menu principale
			System.out.println("-1 : Retour Menu Choix");

			//Choisit le joueur
			Scanner sc2 = new Scanner(System.in);
			action2 = sc2.nextInt();

			//Ajout pour voir si le double input est regl�
			sc2.close();

			//Si le choix -1 est fait alors on revient au menu
			if(action2 == -1)
			{
				AskAction();
				return;
			}

			//Choisit la ressource a voler
			System.out.println("Et quelle ressources voulez vous prendres ?");
			Scanner sc3 = new Scanner(System.in);
			action3 = sc3.nextInt();
			sc3.close();


			SetMode(Mode.Vol);
			//Vol le joueur
			VolRessourceAgresseur(this.ListJoueur.get(action2), this.ListJoueur.get(action2).GetStock().get(action3));
			System.out.println("Vol fait");

			break;


		//Passe en mode observation le temps d'un tour
		case 3 : System.out.println("Passage en mode Observation , vous pouvez punir les joueurs tentant de vous voler \n");
			this.mode = Mode.Observation;
			break;

		//Affiche l'inventaire du joueur et redemande une action a effectuer
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
	 */
	synchronized public ArrayList<Ressources> Observation(ClientInterface j) {
		try{
			return j.GetStock();
		}catch (RemoteException re) { System.out.println(re) ;return null; }

	}



	/** Ajout au stock de ressource le nombre donne
	 * @param r Ressource a ajouter
	 * @param nbr le nombre a ajouter
	 * @return boolean suivant si cela a reussit ou non
	 */
	private boolean AjoutStock(Ressources r, int nbr){

		//Cherche l'index de la ressource
		int indexStock = SearchRessource(r);

		//Si la ressource n'existe pas l'a cree
		if(indexStock == -1)
		{
			Ressources r1 = new Ressources(r.getName(),nbr);
			StockRessources.add(r1);
		}
		else
		{
			//Ajoute le nombre a la ressource
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
			//On test si la ressource est la meme que celle demande
			if(StockRessources.get(i).equals(r))
				return i;
		}

		return index;
	}


	/** Recherche le producteur produisant la ressource demande
	 * @param r Ressource cherche chez les producteurs
	 * @return int l'index dans la liste de producteur du producteur produisant la ressource r
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
					//Si le producteur possede plus que 0 de ressource alors le retourne immediatement
					if(this.ListProducteur.get(i).GetRessources().getExemplaires() > 0)
						return i;
					else index = i;
				}
			}
			catch (RemoteException re) {System.out.println(re) ;}
		}
		return index;
	}


	/** Verifie si le joueur a fini la parti en checkant que toute les ressources ont atteint l'objectif
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

		System.out.println("--------------------------------- \n");
		for(int i=0;i<this.StockRessources.size();i++){
			System.out.println(this.StockRessources.get(i).getName()+" : "+this.StockRessources.get(i).getExemplaires());
		}
		System.out.println("--------------------------------- \n");
	}
}
