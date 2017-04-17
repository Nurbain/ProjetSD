import java.rmi.RemoteException;

public class Producteur extends Client implements ProducteurInterface{

	//Stock de ressource du producteur
	private Ressources Stock;
	private float ratioProd;
	//Nombre de ressource que le producteur peut donner a la fois 
	private int CanGive;

	public Producteur(String name,String nameRessource, int nbrinit,float ratioProd, int CanGive) throws RemoteException {
		super(name);

		this.CanGive = CanGive;
		this.ratioProd = ratioProd;
		this.Stock = new Ressources(nameRessource, nbrinit);
	}

	public Ressources GetStock()
	{
		return this.Stock;
	}

	public synchronized int GetCanGive(){
		return this.CanGive;
	}

	public void run() {
		while(true){
			Production();
			try{
				Thread.sleep(1000);
			}catch(InterruptedException e){System.out.println(e);}
		}
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
		Stock.addRessources((int)(Stock.getExemplaires()*ratioProd));
	}


}
