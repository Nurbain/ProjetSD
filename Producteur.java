import java.rmi.RemoteException;

public class Producteur extends Client{

	//Stock de ressource du producteur
	private Ressources Stock;
	private boolean isEpusable;
	
	public Producteur(String name, Ressources Stock, boolean epusable) throws RemoteException {
		super(name);
		
		this.isEpusable = epusable;
		this.Stock = Stock;
	}

	//Donne les ressources a un joueur
	public void DonneRessource(Joueur j, int nbr)
	{
		
	}
	
	//Produit des ressources
	public void Production()
	{
		
	}
	
	public Ressources GetStock()
	{
		return this.Stock;
	}
	
	public Boolean GetIsEpusable()
	{
		return this.isEpusable;
	}
	
}
