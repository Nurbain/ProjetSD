#!/bin/sh

if [ $# -ne 2 ]
then
	echo "use : $0 <Nom image> <Nom du fichier de log>"
	exit 1
fi

touch InstrGNUPLOT

java GenerateurLog $2 "tmplog" 

nbColonne=`cat tmpParam`

tmp=`expr $nbColonne + 1`

List=`seq -w 3 $tmp`
echo $List

rm tmpParam

tmp=`expr $nbColonne - 1`

Listresult=`seq -w 0 $tmp`

for j in $Listresult
do
	echo "plot \"tmplog$j\" title \"P0\" with linespoints" > InstrGNUPLOT
	for i in $List
	do
	  tmp=`expr $i - 2`
	  echo "replot \"tmplog$j\" using 1:$i title \"P$tmp\" with linespoints" >> InstrGNUPLOT
	done
	echo "set terminal png" >> InstrGNUPLOT
	echo "set output \"$1$j.png\"" >> InstrGNUPLOT
	echo "replot" >> InstrGNUPLOT
	gnuplot InstrGNUPLOT
	rm tmplog$j
done

nbColonne=`cat tmpParam2`

tmp=`expr $nbColonne + 1`

List=`seq -w 3 $tmp`
echo $List

#rm tmpParam2

tmp=`expr $nbColonne - 1`

Listresult=`seq -w 0 $tmp`

for j in $Listresult
do
	echo "plot \"tmplogJ$j\" title \"R0\" with linespoints" > InstrGNUPLOT
	for i in $List
	do
	  tmp=`expr $i - 2`
	  echo "replot \"tmplogJ$j\" using 1:$i title \"R$tmp\" with linespoints" >> InstrGNUPLOT
	done
	echo "set terminal png" >> InstrGNUPLOT
	echo "set output \"$1J$j.png\"" >> InstrGNUPLOT
	echo "replot" >> InstrGNUPLOT
	gnuplot InstrGNUPLOT
	rm tmplogJ$j
done


rm InstrGNUPLOT
