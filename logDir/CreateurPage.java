import java.io.*;

public class CreateurPage {

	private static int nbrjoueur;
	private static int nbrprod;
	private static String isTourParTour = "false";
	private static String Dossier;
	private static int Objectif = 100;
	private static String[] NomsJoueur;
	private static String[] NomsRessources;
	private static int Temps;
	private static String[] Classement;
	 public static void main(String[] args){

		 if (args.length != 3)
		{
				System.out.println("Usage : java CreateurPage <nbrJoueur> <nbrProducteur> <Dossier des images>") ;
				System.exit(0) ;
		}

		nbrjoueur = Integer.parseInt(args[0]);
		nbrprod = Integer.parseInt(args[1]);
		Dossier = args[2]+"/";


		BufferedReader bis = null;
		try{
			//On initialise bis pour lire dans le fichier de log
			bis = new BufferedReader(new FileReader(new File("tmpParam4")));
			String line;
			line = bis.readLine();
			String delims = "[ ]+";
	    NomsJoueur = line.split(delims);
			bis.close();

			bis = new BufferedReader(new FileReader(new File("tmpParam3")));
			line = bis.readLine();
	    NomsRessources = line.split(delims);
			bis.close();

			bis = new BufferedReader(new FileReader(new File("tmpParam5")));
			isTourParTour = bis.readLine();
			line = bis.readLine();
	    Objectif = Integer.parseInt(line);
			bis.close();

			bis = new BufferedReader(new FileReader(new File("tmpTemps")));
			line = bis.readLine();
	    Temps = Integer.parseInt(line);
			bis.close();

			bis = new BufferedReader(new FileReader(new File("tmpClassement")));
			line = bis.readLine();
			delims = "[ ]+";
	    Classement = line.split(delims);
			bis.close();

		}catch (FileNotFoundException e){e.printStackTrace();}
		catch (IOException e){e.printStackTrace();}
		finally{
			try{
				if(bis != null)
					bis.close();
			} catch (IOException e){ e.printStackTrace(); }
		}

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
				+"<li><a id=\"GL\" onclick=\"ChangementGL(this,'"+Dossier+"' )\" >Tous</a></li> \n");

		for(int i = 0; i<nbrjoueur ; i++)
		{
			w.write("<li><a id=\"J"+i+"\" onclick=\"ChangementJoueur(this,'"+Dossier+"', '"+NomsJoueur[i]+"')\" >"+NomsJoueur[i]+"</a></li> \n");
		}

		w.write("</ul></div> \n");


		//Ecriture Producteurs
		w.write("<div class=\"btn-group\" role=\"group\"> \n"
				+"<button class=\"btn btn-default dropdown-toggle\" type=\"button\" id=\"dropdownMenu1\" data-toggle=\"dropdown\" aria-haspopup=\"true\" aria-expanded=\"true\"> Ressources <span class=\"caret\"></span>"
				+"</button> \n"
				+"<ul class=\"dropdown-menu\"> \n");

		for(int i = 0; i<nbrprod ; i++)
		{
			w.write("<li><a id=\""+i+"\" onclick=\"ChangementProducteur(this ,'"+Dossier+"' , '"+NomsRessources[i]+"')\">"+NomsRessources[i]+"</a></li> </li> \n");
		}
		w.write("</ul></div></div><br> \n");


		//Ecriture Graphe
		w.write("<div class=\"row titre\"> \n"
				+"<div class=\"col-md-10\" id=\"graphe\"> \n"
				+"<h3 id=\"titlegraphe\">Graphe de Tous les Joueurs</h3> \n"
				+"<img id=\"grapheImg\" src=\""+Dossier+"GL.png\"> \n"
				+"</div> \n");

		//Ecriture Param�tre
		w.write("<div class=\"col-md-2\"> \n"
				+"<h4 id=\"Param\">Parametre de Partie</h4> \n"
				+"<p>Joueurs : "+nbrjoueur+"</p> \n"
				+"<p>Producteurs : "+nbrprod+"</p> \n"
				+"<p>Tour par tour :"+isTourParTour+"</p> \n"
				+"<p>Objectif :"+ Objectif +"</p> \n"
				+"<p>Durée partie :"+ ((double)Temps/1000) +"s</p> \n"
				+"<h4 id=\"Classement\">Classement de Partie</h4> \n");

		for(int i = 0; i<Classement.length ; i++)
		{
			w.write("<p>"+(i+1)+" : "+Classement[i]+"</p> \n");
		}

		w.write("</div></div> \n");

		//Ecriture Description
		w.write("<div class=\"row\" id=\"description\"> \n"
				+"<h3>Description projet</h3> \n"
				+"<p>Projet de Systeme distribue du Semestre 6 de printemps 2017</p> \n"
				+"<p>Ce mini-jeu met en place des joueurs ayant une personnalite propre ayant pour but de recuperer des ressources produites par des producteurs uniques</p> \n"
				+"<p>Les joueurs doivent atteindre un nombre fixe d'exemplaire de ressource pour chaque type de ressources</p> \n"
				+"</div></div> \n");

		//Ecriture Script
		w.write("<script src=\"ChangementImg.js\"></script> \n"
				+"<script src=\"http://ajax.googleapis.com/ajax/libs/jquery/1.10.2/jquery.min.js\"></script> \n"
				+"<script src=\"https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js\"></script> \n"
				+"</body></html>");
		w.close();
	}

}
