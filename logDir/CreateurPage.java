package logDir;

import java.io.*;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

public class CreateurPage {

	private static int nbrjoueur;
	private static int nbrprod;
	private static String isTourParTour = "false";
	private static String nomFichier;
	private static String Dossier;
	private static int Objectif = 100;

	 public static void main(String[] args){

		 if (args.length != 5 || (args.length != 4))
		{
				System.out.println("Usage : java CreateurPage <nbrJoueur> <nbrProducteur> <Nom fichierLog> <Nom de fichier des images> <Dossier des images>") ;
				System.exit(0) ;
		}

		/*BufferedReader bis =null;

		try{
	      bis = new BufferedReader(new FileReader(new File(args[0])));
	      String line;
	      LectureMode(bis);
	      System.out.println("Mode = "+mode);
	      LectureObjectif(bis);
	      System.out.println("Objectif = "+Objectif);
	      while((line = bis.readLine()) != null){

	        System.out.println(line);
	        if(line.equals("Producteurs :")){
	          System.out.println("Liste Prod :");
	          LectureProducteur(bis);
	          break;
	        }
	        if(line.equals("Joueurs :")){
	          System.out.println("Liste Joueur :");
	          LectureJoueur(bis);

	        }
				}
		}*/

		nbrjoueur = args[0];
		nbrProducteur = args[1];
		nomFichier = args[3];

		 if(args.length != 4)
			 Dossier = args[4]+"/";

		 try {
			Creation();
		}
		 catch (FileNotFoundException e) {e.printStackTrace();}
		 catch (UnsupportedEncodingException e) {e.printStackTrace();}

	 }

	public static void Creation() throws FileNotFoundException, UnsupportedEncodingException
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
			w.write("<li><button id=\""+nomFichier+"J"+i+"\" onclick=\"ChangementJoueur(this,'"+Dossier+"' )\" >Joueur "+(i+1)+"</button></li> \n");
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
			w.write("<li><button id=\""+nomFichier+"P"+i+"\" onclick=\"ChangementProducteur(this ,'"+Dossier+"' )\">Joueur "+(i+1)+"</button></li> </li> \n");
		}
		w.write("</ul></div></div><br> \n");


		//Ecriture Graphe
		w.write("<div class=\"row titre\"> \n"
				+"<div class=\"col-md-10\" id=\"graphe\"> \n"
				+"<h3 id=\"titlegraphe\">Tout les joueurs</h3> \n"
				+"<img id=\"grapheImg\" src=\"test.png\"> \n"
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
