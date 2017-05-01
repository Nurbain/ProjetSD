La partie se lance grâce à l’utilisation d’un script bash utilisable comme ceci :
./Launcher4 <Port> <NbrProducteur>  <NbrJoueur> <FichierLogGeneral>



Lorsque la commande est lancé l’utilisateur peut facilement paramétrer tous les aspects du jeu. 
Tout d’abords il doit indiquer si il y’a un joueur réel, si oui alors la partie sera en tour par tour, 
le cas échéant alors il doit rentrer si le jeu se fait tour par tour pour ensuite entrer le nombre objectif à atteindre pour chaques ressources.

Par la suite pour chaques producteurs l’utilisateur doit donner les informations demandées, 
ainsi dans l’ordre : 
	-le nom de la ressource
	-son ratio de production
	-le nombre de ressource disponible en début de partie
	-le nombre pouvant être données au maximum.


Enfin l’utilisateur peut choisir la personnalité de chaque joueur en rentrant le nombre correspondant grâce au menu affiché.


La génération de la Simulation se fait lorsque la partie est fini par la commande : 
./genGenuplotInstr.sh <RepertoireDestination> <FichierLogGeneral>


Une page web est ainsi créé pour pouvoir voir les résultats à l’aide de graphiques.



Pour faciliter le lancement de la partie, nous avons mis quelques jeux de Test lançables par la commande :
cat JeuxTest.txt | ./Launcher4 <Port> <NbrProducteur>  <NbrJoueur> <FichierLogGeneral>

