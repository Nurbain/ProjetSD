import java.rmi.RemoteException;

public class Producteur extends Client implements ProducteurInterface{

	//Stock de ressource du producteur
	private Ressources Stock;

	public Producteur(String name,String nameRessource, int nbrinit) throws RemoteException {
		super(name);

		this.Stock = new Ressources(nameRessource, nbrinit);
	}

	public Ressources GetStock()
	{
		return this.Stock;
	}



	//Donne les ressources a un joueur
	public boolean PrendreRessource(int nbr)
	{
		/*Si faux refuser
		return Stock.takeRessources(nbr);
		*/
		return true;
	}

	//Produit des ressources
	public void Production()
	{
		// int nombre = Stock.getExemplaires();
		// int pourcentActuel = (this.NombreMax*100)/nombre;
		// int pourcentToAdd = (100-pourcentActuel)/10;
		//
		// int toAdd = (pourcentToAdd*this.NombreMax)/100;

		// Stock.addRessources(toAdd);
	}


}
