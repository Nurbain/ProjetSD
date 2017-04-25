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

echo "plot \"tmplog\" title \"P0\" with linespoints" > InstrGNUPLOT
for i in $List
do
  tmp=`expr $i - 2`
  echo "replot \"tmplog\" using 1:$i title \"P$tmp\" with linespoints" >> InstrGNUPLOT
done
echo "set terminal png" >> InstrGNUPLOT
echo "set output \"$1.png\"" >> InstrGNUPLOT
echo "replot" >> InstrGNUPLOT

gnuplot InstrGNUPLOT

rm tmplog
rm InstrGNUPLOT
