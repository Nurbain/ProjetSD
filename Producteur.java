import java.rmi.RemoteException;

public class Producteur extends Client{

	//Stock de ressource du producteur
	private int NombreMax;
	private Ressources Stock;
	private boolean isEpusable;
	
	public Producteur(String name,String nameRessource, boolean epusable, int nbrMax) throws RemoteException {
		super(name);
		this.isEpusable = epusable;
		this.NombreMax = nbrMax;
		
		this.Stock = new Ressources(nameRessource, nbrMax);
	}

	public Ressources GetStock()
	{
		return this.Stock;
	}
	
	public Boolean GetIsEpusable()
	{
		return this.isEpusable;
	}
	
	
	//Donne les ressources a un joueur
	public boolean DonneRessource(Joueur j, int nbr)
	{
		j.AjoutStock(nbr, this.Stock);
		return Stock.takeRessources(nbr);
	}
	
	//Produit des ressources
	public void Production()
	{
		int nombre = Stock.getExemplaires();
		int pourcentactuel = (this.NombreMax*100)/nombre;
		int pourcenttoadd = (100-pourcentactuel)/10;
		
		int toadd = (pourcenttoadd*this.NombreMax)/100;
		
		Stock.addRessources(toadd);
	}

	
}
