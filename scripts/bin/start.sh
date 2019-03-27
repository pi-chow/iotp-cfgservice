if [ ! -d ./pids ]; then
    mkdir ./pids
fi

PIDFILE=./pids/id

nohup java -jar iotp-cfgservice-0.9.0.jar --logging.config=./logback.xml >/dev/null &

echo $! > $PIDFILE
echo STARTED