#!/bin/sh

if [ $# -ne 3 ]
then
	echo "use : $0 <nb Joueur> <Nom image> <Nom du fichier de log>"
	exit 1
fi

touch InstrGNUPLOT

java GenerateurLog $3 "tmplog"

echo "plot \"tmplog\" title \"P1\" with linespoints" > InstrGNUPLOT
echo "replot \"tmplog\" using 1:3 title \"P2\" with linespoints" >> InstrGNUPLOT
echo "set terminal png" >> InstrGNUPLOT
echo "set output \"$2\"" >> InstrGNUPLOT
echo "replot" >> InstrGNUPLOT

gnuplot InstrGNUPLOT

rm tmplog
rm InstrGNUPLOT

# plot "testWrite.txt" title "P1" with linespoints
# replot "testWrite.txt" using 1:3 title "P2" with linespoints
# set terminal png
# set output "resultat.png"
# replot
# echo ""
