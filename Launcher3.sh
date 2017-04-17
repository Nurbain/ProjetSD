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

for i in $ListProd
do
  java ProducteurServ $1 P$i R$i 100 1 10 &
done

for i in $ListProd
do
  java JoueurServ $1 J$i &
done

exit 0
