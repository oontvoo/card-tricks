card-tricks
===========
Well, it's Friday 13th, so it's only fitting that we are doing some card-tricks. ;) Good luck!


Desktop App
----------
To build an executable jar (for running locally as a Desktop application):

    mvn clean package

This will produce a jar called `*-jar-with-dependencies.jar` under `target`.
You can either click on the jar to run it or, from the command line, run
    `java -jar target/*-jar-with-dependencies.jar`


Applet
--------

To build an applet `jar` to run in a browser

    mvn clean package
	
This will build the `jar` and also sign it. (The certificate is only valid for six months!)
See the simple `index.html` for example of how to use the applet jar.
