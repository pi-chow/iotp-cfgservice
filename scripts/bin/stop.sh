PIDFILE=./pids/id

if [ ! -f $PIDFILE ]
    then
        echo "error: count not find file $PIDFILE"
        exit 1
    else
        kill -9 $(cat $PIDFILE)
        rm $PIDFILE
        echo STOPPED
fi