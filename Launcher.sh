#!/bin/sh

if [ $# -ne 1 ]
then
	echo "use : $0 <port num>"
	exit 1
fi

listpts=`who | grep pts | cut -d' ' -f2 | cut -d/ -f2`

for i in $listpts
do 
	java ClientServ $1 C$i >> /dev/pts/$i &
done

sleep 2;

java Coordinateur localhost $1 $listpts &

exit 0
