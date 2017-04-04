#!/bin/sh

if [ $# -ne 1 ]
then
	echo "use : $0 <port num>"
	exit 1
fi


for i in `who | grep pts | cut -d' ' -f2 | cut -d/ -f2`
do 
	java ClientServ $1 C$i >> /dev/pts/$i &
done

exit 0
