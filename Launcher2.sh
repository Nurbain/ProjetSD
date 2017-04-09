#!/bin/sh

if [ $# -ne 1 ]
then
	echo "use : $0 <port num>"
	exit 1
fi

listpts=`ls /dev/pts | grep -E -x '[0-9]+'`

echo $listpts;

for i in $listpts
do
	java ClientServ $1 C$i >> /dev/pts/$i &
done

sleep 2;

java Coordinateur localhost $1 $listpts &

exit 0
