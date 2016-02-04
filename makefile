# Really simple way to build this

all: compile

compile:
	if [ ! -d "classes" ]; then mkdir classes; fi;
	find src -name *.java | xargs javac -cp src/ -d classes/
	
build: compile
	if [ ! -d "build" ]; then mkdir build; fi;
	jar cfe build/htremote.jar com.zk.oppo.remote.OppoRemote -C classes .

clean:
	rm -rf build/*
	rm -rf classes/*
