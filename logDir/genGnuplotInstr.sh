#!/bin/sh

if [ $# -ne 3 ]
then
	echo "use : $0 <nb Joueur> <Nom image> <Nom du fichier de log>"
	exit 1
fi

tmp=`expr $1 + 1`

List=`seq -w 3 $tmp`
echo $List

touch InstrGNUPLOT

java GenerateurLog $3 "tmplog" >> /dev/null

echo "plot \"tmplog\" title \"P1\" with linespoints" > InstrGNUPLOT
for i in $List
do
  echo "replot \"tmplog\" using 1:$i title \"P2\" with linespoints" >> InstrGNUPLOT
done
echo "set terminal png" >> InstrGNUPLOT
echo "set output \"$2\"" >> InstrGNUPLOT
echo "replot" >> InstrGNUPLOT

gnuplot InstrGNUPLOT

rm tmplog
rm InstrGNUPLOT
