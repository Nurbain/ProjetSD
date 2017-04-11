import java.rmi.RemoteException;

public class Producteur extends Client implements ProducteurInterface{

	//Stock de ressource du producteur
	private Ressources Stock;
	private float ratioProd;
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
	public int PrendreRessource()
	{
		/*Si faux refuser
		return Stock.takeRessources(nbr);
		*/
		return 0;
	}

	//Produit des ressources
	public synchronized void Production()
	{
		Stock.addRessources((int)(Stock.getExemplaires()*ratioProd));
	}


}
