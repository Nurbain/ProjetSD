La partie se lance grâce à l’utilisation d’un script bash utilisable comme ceci :
./Launcher4 NomServeur Port NbrProducteur  NbrJoueur FichierLogGeneral 



Lorsque la commande est lancée l’utilisateur peut facilement paramétrer tous les aspects du jeu.
Tout d’abord il doit indiquer s'il y a un joueur réel, si oui alors la partie sera en tour par tour
Le cas échéant alors il doit rentrer si le jeu se fait tour par tour pour ensuite entrer le nombre objectif à atteindre pour chaque ressource.

Par la suite pour chaque producteur l’utilisateur doit donner les informations demandées
Ainsi dans l’ordre : 
-le nom de la ressource
-son ratio de production
-le nombre de ressources disponibles en début de partie
-le nombre pouvant être donné au maximum.

Enfin l’utilisateur peut choisir la personnalité de chaque joueur en rentrant le nombre correspondant grâce au menu affiché.


La génération de la simulation se fait lorsque la partie est fini par la commande : 
./genGenuplotInstr.sh RepertoireDestination FichierLogGeneral


Une page web est ainsi créée pour pouvoir voir les résultats à l’aide de graphiques.



Pour faciliter le lancement de la partie, nous avons mis quelques jeux de Test lançables par la commande :
cat JeuxTest.txt | ./Launcher4 NomServeur Port NbrProducteur  NbrJoueur FichierLogGeneral

