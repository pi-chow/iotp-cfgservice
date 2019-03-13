if [ ! -d ./pids ]; then
    mkdir ./pids
fi

PIDFILE=./pids/id

nohup java -jar IOTP-CfgService.jar --logging.config=./logback.xml >/dev/null &

echo $! > $PIDFILE
echo STARTED