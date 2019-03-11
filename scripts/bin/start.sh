if [ ! -d ./pids ]; then
    mkdir ./pids
fi

PIDFILE=./pids/id

nohup java -jar iot-cfg.jar --logging.config=./logback.xml >/dev/null &

echo $! > $PIDFILE
echo STARTED