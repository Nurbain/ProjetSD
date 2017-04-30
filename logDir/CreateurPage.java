import java.io.FileNotFoundException;
import java.io.PrintWriter;

public class CreateurPage {

	private static int nbrjoueur;
	private static int nbrprod;
	private static String isTourParTour = "false";
	private static String nomFichier;
	private static String Dossier;
	private static int Objectif = 100;

	 public static void main(String[] args){

		 if (args.length != 3)
		{
				System.out.println(args.length);
				System.out.println("Usage : java CreateurPage <nbrJoueur> <nbrProducteur> <Dossier des images>") ;
				System.exit(0) ;
		}

		nbrjoueur = Integer.parseInt(args[0]);
		nbrprod = Integer.parseInt(args[1]);
		Dossier = args[2]+"/";

		 try {
			Creation();
		}
		 catch (FileNotFoundException e) {e.printStackTrace();}

	 }

	public static void Creation() throws FileNotFoundException
	{
		PrintWriter w = new PrintWriter("Visualisation.html");

		//Ecriture Head
		w.write("<!DOCTYPE html>\n"+
				"<head>\n"
				+ "<meta charset=\"utf-8\"></meta>"
				+"<title>Projet SD</title>\n"
				+"<link href=\"style.css\" rel=\"stylesheet\"></link> \n"
				+"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css\"> \n"
				+"<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap-theme.min.css\"> \n"
				+"</head>"
				);

		//Ecriture Titre
		w.write("<body> \n"
				+"<div class=\"container \"> \n"
				+"<div class=\"row titre\"> \n"
				+"<h1 id=\"title\">Visualisation Projet SD</h1> \n"
				+"<h3 id=\"author\">Quentin Wendling & Nathan Urbain</h3> \n"
				+"</div><br><br> \n"
				);


		//Ecriture Joueurs
		w.write("<div class= \"row\"> \n"
				+"<div class=\"btn-group\" role=\"group\"> \n"
				+"<button class=\"btn btn-default dropdown-toggle\" type=\"button\" id=\"dropdownMenu1\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\"> Joueurs <span class=\"caret\"></span> "
				+"</button>\n"
				+"<ul class=\"dropdown-menu\"> \n"
				+"<li><a href=\"#\">Tous</a></li> \n");

		for(int i = 0; i<nbrjoueur ; i++)
		{
			w.write("<li><button id=\"J"+i+"\" onclick=\"ChangementJoueur(this,'"+Dossier+"' )\" >Joueur "+(i+1)+"</button></li> \n");
		}

		w.write("</ul></div> \n");


		//Ecriture Producteurs
		w.write("<div class=\"btn-group\" role=\"group\"> \n"
				+"<button class=\"btn btn-default dropdown-toggle\" type=\"button\" id=\"dropdownMenu1\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\"> Producteurs <span class=\"caret\"></span>"
				+"</button> \n"
				+"<ul class=\"dropdown-menu\"> \n"
				+"<li><a href=\"#\">Tous</a></li> \n");

		for(int i = 0; i<nbrprod ; i++)
		{
			w.write("<li><button id=\""+i+"\" onclick=\"ChangementProducteur(this ,'"+Dossier+"' )\">Joueur "+(i+1)+"</button></li> </li> \n");
		}
		w.write("</ul></div></div><br> \n");


		//Ecriture Graphe
		w.write("<div class=\"row titre\"> \n"
				+"<div class=\"col-md-10\" id=\"graphe\"> \n"
				+"<h3 id=\"titlegraphe\">Tout les joueurs</h3> \n"
				+"<img id=\"grapheImg\" src=\""+Dossier+"J0.png\"> \n"
				+"</div> \n");

		//Ecriture Param�tre
		w.write("<div class=\"col-md-2\"> \n"
				+"<h4 id=\"Param\">Parametre de Partie</h4> \n"
				+"<p>Joueurs : "+nbrjoueur+"</p> \n"
				+"<p>Producteurs : "+nbrprod+"</p> \n"
				+"<p>Tour par tour :"+isTourParTour+"</p> \n"
				+"<p>Objectif :"+ Objectif +"</p> \n"
				+"</div></div> \n");


		//Ecriture Description
		w.write("<div class=\"row\" id=\"description\"> \n"
				+"<h3>Description projet</h3> \n"
				+"<p>Bla bla</p> \n"
				+"</div></div> \n");

		//Ecriture Script
		w.write("<script src=\"ChangementImg.js\"></script> \n"
				+"<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script> \n"
				+"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script> \n"
				+"</body></html>");
		w.close();
	}

}
