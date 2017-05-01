#!/bin/sh

if [ $# -ne 4 ]
then
	echo "use : $0 <port num> <nb prod> <nb Joueur> <Nom fichier log>"
	exit 1
fi

ListProd=`seq -w 1 $2`
echo $ListProd

ListJoueur=`seq -w 1 $3`
echo $ListJoueur

ListProdPropre=' '

while [ "$ANSWERreel" != "y" -a "$ANSWERreel" != "n" ]
do
	read -p "Jouer avec un joueur reel ? (y/n)" ANSWERreel
done

if [ "$ANSWERreel" = "y" ]
then
	xterm -e java JoueurServ $1 JoueurReel y y &
	ANSWER="y"
fi

while [ "$ANSWER" != "y" -a "$ANSWER" != "n" ]
do
	read -p "Jouer en tour par tour ? (y/n)" ANSWER
done

while [ "$ANSWERFIN" != "y" -a "$ANSWERFIN" != "n" ]
do
	read -p "Faire une fin brute ? (y/n)" ANSWERFIN
done
if [[ "$ANSWERFIN" = "y" ]]; then
	ANSWERFIN="Brute"
else
	ANSWERFIN="Attente"
fi

read -p "Nombre d'exemplaire de chaque ressources pour la victoire :" objectif
if [ -z $objectif ]
then
	echo "Valeur par defaut 100"
	nomRessource=100
fi

for i in $ListProd
do
  ListProdPropre="$ListProdPropre P$i"
done

echo $ListProdPropre

ListJoueurPropre=' '

for i in $ListJoueur
do
  ListJoueurPropre="$ListJoueurPropre J$i"
done

echo $ListJoueurPropre

for i in $ListProdPropre
do
	echo "Parametrage de $i"
	read -p "Nom de la ressources produite :" nomRessource
	if [ -z $nomRessource ]
	then
		echo "Valeur par defaut R$i"
		nomRessource=R$i
	fi
	read -p "Ratio de production :" ratioProd
	if [ -z $ratioProd ]
	then
		echo "Valeur par defaut 0.25"
		ratioProd=0.25
	fi
	read -p "Nombre de ressoueces initiales :" nbinit
	if [ -z $nbinit ]
	then
		echo "Valeur par defaut 100"
		nbinit=100
	fi
	read -p "Nombre de ressources pouvant etre données : " canGive
	if [ -z $canGive ]
	then
		echo "Valeur par defaut 10"
		canGive=10
	fi
  xterm -e java ProducteurServ $1 $i $nomRessource $nbinit $ratioProd $canGive $ANSWER &
done

for i in $ListJoueurPropre
do
	echo "Parametrage $i :"
	echo "Choix personnalite :"
	echo "1. Voleur"
	echo "2. Cooperatif"
	echo "3. Individuel"
	echo "4. Stratege"
	echo "5. Mefiant"
	echo "6. Rancunier"
	p=Individuel
	read -p "Choix :" choixPerso
	if [ -z $choixPerso ]
	then
		choixPerso="3"
	fi
	if [ $choixPerso = "1" ]
	then
		p=Voleur
	fi
	if [ $choixPerso = "2" ]
	then
		p=Cooperatif
	fi
	if [ $choixPerso = "3" ]
	then
		p=Individuel
	fi
	if [ $choixPerso = "4" ]
	then
		p=Stratege
	fi
	if [ $choixPerso = "5" ]
	then
		p=Mefiant
	fi
	if [ $choixPerso = "6" ]
	then
		p=Rancunier
	fi
	echo "Choix = $p"
  xterm -e java JoueurServ $1 $i $ANSWER n $p $objectif &
done

xterm -e java ObservateurServ $1 O1 $ANSWER $4 $ANSWERFIN &

sleep 2;

echo "wait"

if [ "$ANSWERreel" = "y" ]
then
	ListJoueurPropre="$ListJoueurPropre JoueurReel"
fi

java Coordinateur localhost $1 $ListJoueurPropre $ListProdPropre O1 &

exit 0
n
n
100
