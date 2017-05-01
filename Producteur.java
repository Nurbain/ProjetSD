import java.rmi.RemoteException;

public class Producteur extends Client{
  static final long serialVersionUID = 42;
	//Stock de ressource du producteur
	private Ressources Stock;

	//Ratio de production de la ressource 
	private float ratioProd;

	//Nombre de ressource que le producteur peut donner a la fois
	private int CanGive;
  private boolean tourParTour;

	public Producteur(String name,String ServerName,String NumPort,String NomServise,String nameRessource, int nbrinit,float ratioProd, int CanGive,boolean tourParTour) throws RemoteException {
		super(name,ServerName,NumPort,NomServise);

		this.CanGive = CanGive;
		this.ratioProd = ratioProd;
		this.Stock = new Ressources(nameRessource, nbrinit);
		this.monType = Type.Producteur;
    this.tourParTour=tourParTour;
	}


	//Fonction demarent le producteur
  public void startAgent(){
		System.out.println("Start");
    StartTimer = System.currentTimeMillis();
    if(!tourParTour){
  		Thread t=new Thread(this);
  		t.start();
    }
	}

	//Ressource retournant l'etat du stock actuel
	public Ressources GetRessources()
	{
		return new Ressources(this.Stock.getName(),this.Stock.getExemplaires());
	}

	//Retourne ce que peut donner au maxium en une fois le producteur
	public synchronized int GetCanGive(){
		return this.CanGive;
	}

	//Fonction run du producteur en mode automatique
	public void run() {
		//Tant que la parti n'est pas fini alors produit
		while(true && !finParti){
			//Pour eviter que le producteur produise trop
			if(this.Stock.getExemplaires()<10000)
			{
		    System.out.println("Production");
				//Produit la ressource 
				Production();
		    System.out.println(System.currentTimeMillis()-StartTimer);
		    System.out.println("Possede : "+this.Stock.getExemplaires()+" de "+this.Stock.getName());
			}
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){System.out.println(e);}
		}
	}

	//Fonction de run en mode tour par tour
  public void tourDeJeu(){
		if(this.Stock.getExemplaires()<10000)
		{
    System.out.println("Production");
    Production();
    System.out.println("Possede : "+this.Stock.getExemplaires()+" de "+this.Stock.getName());
		}
    try{
      Thread.sleep(1000);
    }catch(InterruptedException e){System.out.println(e);}
  }


	//Donne les ressources a un joueur
	public synchronized int PrendreRessource()
	{
		//Si possede plus que ce qu'il peut donner en une fois alors donne ce nombre 
		if(Stock.getExemplaires() >= CanGive){
			Stock.takeRessources(CanGive);
			return CanGive;
		}
		//Sinon donne le nombre qu'il lui reste
		int tmp = Stock.getExemplaires();
		Stock.takeRessources(tmp);
		return tmp;
	}

	//Produit des ressources
	public synchronized void Production()
	{
		//Si possede moi que ce qu'il peut donner en une fois alors se le rajoute 
    if(Stock.getExemplaires()<CanGive){
      Stock.addRessources(CanGive);
			//Ajoute les logs de la production en detail
      this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,this.monType+"  "+this.name+" Produit "+Stock.getName()+"  "+CanGive));
      return;
    }
		//Sinon produit le nombre de ressource fois sont ratio de production
    	int nombre = (int)(Stock.getExemplaires()*ratioProd);
		Stock.addRessources(nombre);
			//Ajoute les logs de la production en detail
      this.LogPerso.add(new LogEntries(System.currentTimeMillis()-StartTimer,this.monType+"  "+this.name+" Produit "+Stock.getName()+"  "+nombre));
	}


}
