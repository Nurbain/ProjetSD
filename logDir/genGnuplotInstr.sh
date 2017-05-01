#!/bin/sh

if [ $# -ne 2 ]
then
	echo "use : $0 <Nom dossier image> <Nom du fichier de log>"
	exit 1
fi

#Si le répertoire destination existe deja on le supprime
if [ -e $1 ]
then
	rm -r $1
fi

#On crée le répertoire de destination
mkdir $1

#On crée le fichier d'instruction GNUplot
touch InstrGNUPLOT

#On genere tous les fichier GNUplot qui commence par tmplog
java GenerateurLog $2 "tmplog"

#On récupère la liste des noms des joueurs et des ressources
ListNomRessources=`cat tmpParam3`
ListNomJoueurs=`cat tmpParam4`
rm tmpParam3
rm tmpParam4

#On recupere le nombre de Ressources qu'on stock dans nbrRessources
nbColonne=`cat tmpParam2`
nbrRessources=$nbColonne

#On recupere le nombre de Joueur qu'on stock dans nbrjoueur
nbColonne=`cat tmpParam`
nbrjoueur=$nbColonne

#Fichier GNUPLOT de la forme x yj1 yj2 ... yjn
#tmp correspond au numero de la colonne yjn
tmp=`expr $nbrjoueur + 1`

# x et yj1 deja trouvé du  coup on commence à la 3eme colonne
List=`seq -w 3 $tmp`

#Suppresion des fichiers temporaire
rm tmpParam
rm tmpParam2

tmp=`expr $nbrRessources - 1`

#pour n ressources liste de 0 à n-1 pour numéroter nos ressources et trouver les fichiers
Listresult=`seq -w 0 $tmp`

#Pour chaque ressources de 0 à n-1
for j in $Listresult
do
	ListNomJoueurstmp="$ListNomJoueurs"
	NomJoueur=`echo "$ListNomJoueurstmp" |cut -d' ' -f1`
	ListNomJoueurstmp=`echo "$ListNomJoueurstmp" |sed 's/[^ ]* *\(.*\)$/\1/'`
	echo "plot \"tmplog$j\" title \"$NomJoueur\" with linespoints" > InstrGNUPLOT
	#Pour chaque joueur de 1 à n-1
	#Joueur n = colonne n+2
	for i in $List
	do
		NomJoueur=`echo "$ListNomJoueurstmp" |cut -d ' ' -f 1`
		ListNomJoueurstmp=`echo "$ListNomJoueurstmp" |sed 's/[^ ]* *\(.*\)$/\1/'`
	  tmp=`expr $i - 2`
		#On trace la courbe du joueur tmp
	  echo "replot \"tmplog$j\" using 1:$i title \"$NomJoueur\" with linespoints" >> InstrGNUPLOT
	done
	echo "set terminal png" >> InstrGNUPLOT
	echo "set output \"$1/$j.png\"" >> InstrGNUPLOT
	echo "replot" >> InstrGNUPLOT
	gnuplot InstrGNUPLOT
	rm tmplog$j
done
#Fin de la creation des graphes de ressource

#Creation du graphe de l'avancement générale des joueurs
ListNomJoueurstmp="$ListNomJoueurs"
NomJoueur=`echo "$ListNomJoueurstmp" |cut -d ' ' -f 1`
ListNomJoueurstmp=`echo "$ListNomJoueurstmp" |sed 's/[^ ]* *\(.*\)$/\1/'`
echo "plot \"tmplogGL\" title \"$NomJoueur\" with linespoints" > InstrGNUPLOT
for i in $List
do
	NomJoueur=`echo "$ListNomJoueurstmp" |cut -d ' ' -f 1`
	ListNomJoueurstmp=`echo "$ListNomJoueurstmp" |sed 's/[^ ]* *\(.*\)$/\1/'`
  tmp=`expr $i - 2`
  echo "replot \"tmplogGL\" using 1:$i title \"$NomJoueur\" with linespoints" >> InstrGNUPLOT
done
echo "set terminal png" >> InstrGNUPLOT
echo "set output \"$1/GL.png\"" >> InstrGNUPLOT
echo "replot" >> InstrGNUPLOT
gnuplot InstrGNUPLOT
rm tmplogGL
#Fin creation du graphe de l'avancement générale des joueurs


tmp=`expr $nbrRessources + 1`

List=`seq -w 3 $tmp`

tmp=`expr $nbrjoueur - 1`

Listresult=`seq -w 0 $tmp`

for j in $Listresult
do
	ListNomRessourcestmp="$ListNomRessources"
	NomRessources=`echo "$ListNomRessourcestmp" |cut -d ' ' -f 1`
	ListNomRessourcestmp=`echo "$ListNomRessourcestmp" |sed 's/[^ ]* *\(.*\)$/\1/'`
	echo "plot \"tmplogJ$j\" title \"$NomRessources\" with linespoints" > InstrGNUPLOT
	for i in $List
	do
		NomRessources=`echo "$ListNomRessourcestmp" |cut -d ' ' -f 1`
		ListNomRessourcestmp=`echo "$ListNomRessourcestmp" |sed 's/[^ ]* *\(.*\)$/\1/'`
	  tmp=`expr $i - 2`
	  echo "replot \"tmplogJ$j\" using 1:$i title \"$NomRessources\" with linespoints" >> InstrGNUPLOT
	done
	echo "set terminal png" >> InstrGNUPLOT
	echo "set output \"$1/J$j.png\"" >> InstrGNUPLOT
	echo "replot" >> InstrGNUPLOT
	gnuplot InstrGNUPLOT
	rm tmplogJ$j
done



rm InstrGNUPLOT

#Parti de creation de la page web, faut trouver un moyen de chopper tour par tour
java CreateurPage $nbrjoueur $nbrRessources $1
