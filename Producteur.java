import java.rmi.RemoteException;

public class Producteur extends Client{
  static final long serialVersionUID = 42;
	//Stock de ressource du producteur
	private Ressources Stock;
	private float ratioProd;
	//Nombre de ressource que le producteur peut donner a la fois
	private int CanGive;
  private boolean tourParTour;

	public Producteur(String name,String nameRessource, int nbrinit,float ratioProd, int CanGive,boolean tourParTour) throws RemoteException {
		super(name);

		this.CanGive = CanGive;
		this.ratioProd = ratioProd;
		this.Stock = new Ressources(nameRessource, nbrinit);
		this.monType = Type.Producteur;
    this.tourParTour=tourParTour;
	}

  public void startAgent(){
		System.out.println("Start");
    if(!tourParTour){
  		Thread t=new Thread(this);
  		t.start();
    }
	}

	public Ressources GetRessources()
	{
		return new Ressources(this.Stock.getName(),this.Stock.getExemplaires());
	}

	public synchronized int GetCanGive(){
		return this.CanGive;
	}

	public void run() {
		while(true && !finParti){
      System.out.println("Production");
			Production();
      System.out.println("Possede : "+this.Stock.getExemplaires()+" de "+this.Stock.getName());
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){System.out.println(e);}
		}
	}

  public void tourDeJeu(){
    System.out.println("Production");
    Production();
    System.out.println("Possede : "+this.Stock.getExemplaires()+" de "+this.Stock.getName());
    try{
      Thread.sleep(1000);
    }catch(InterruptedException e){System.out.println(e);}
  }


	//Donne les ressources a un joueur
	public synchronized int PrendreRessource()
	{
		if(Stock.getExemplaires() >= CanGive){
			Stock.takeRessources(CanGive);
			return CanGive;
		}
		int tmp = Stock.getExemplaires();
		Stock.takeRessources(tmp);
		return tmp;
	}

	//Produit des ressources
	public synchronized void Production()
	{
    if(Stock.getExemplaires()<CanGive){
      Stock.addRessources(CanGive);
      try {obs.generationLog(this.name,this.monType,Action.Production,this.Stock,CanGive);}
      catch (RemoteException e) {	System.out.println(e);}
      return;
    }
    	int nombre = (int)(Stock.getExemplaires()*ratioProd);
		Stock.addRessources(nombre);
		try {obs.generationLog(this.name,this.monType,Action.Production,this.Stock,nombre);}
	    catch (RemoteException e) {	System.out.println(e);}
	}


}
