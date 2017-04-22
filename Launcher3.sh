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

read -p "Jouer avec un joueur reel ? (y/n)" ANSWERreel

while [ "$ANSWERreel" != "y" -a "$ANSWERreel" != "n" ]
do
	read -p "Jouer avec un joueur reel ? (y/n)" ANSWERreel
done

if [ "$ANSWERreel" = "y" ]
then
	xterm -e java JoueurServ $1 JoueurReel y y &
	ANSWER="y"
	echo "ahhhhhhh"
fi

while [ "$ANSWER" != "y" -a "$ANSWER" != "n" ]
do
	read -p "Jouer en tour par tour ? (y/n)" ANSWER
done

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
  xterm -e java ProducteurServ $1 $i R$i 100 0.25 10 $ANSWER &
done

for i in $ListJoueurPropre
do
  xterm -e java JoueurServ $1 $i $ANSWER n &
done

xterm -e java ObservateurServ $1 O1 $ANSWER $4 &

sleep 2;

echo "wait"

if [ "$ANSWERreel" = "y" ]
then
	ListJoueurPropre="$ListJoueurPropre JoueurReel"
fi

java Coordinateur localhost $1 $ListJoueurPropre $ListProdPropre O1 &

exit 0
