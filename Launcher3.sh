#!/bin/sh

if [ $# -ne 3 ]
then
	echo "use : $0 <port num> <nb prod> <nb Joueur>"
	exit 1
fi

ListProd=`seq -w 1 $2`
echo $ListProd

ListJoueur=`seq -w 1 $3`
echo $ListJoueur

ListProdPropre=' '

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
  xterm -e java ProducteurServ $1 $i R$i 100 1 10 &
done

for i in $ListJoueurPropre
do
  xterm -e java JoueurServ $1 $i &
done

exit 0
