#!/bin/sh

if [ $# -lt 1 ]
then
	echo "use : $0 <port num> <list pts>"
	exit 1
fi

port=$1
shift;

for i in $*
do 
	java ClientServ $port C$i >> /dev/pts/$i &
done
