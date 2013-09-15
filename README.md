card-tricks
===========
Well, it's Friday 13th, so it's only fitting that we are doing some card-tricks. ;) Good luck!


Desktop App
----------
To build an executable jar (for running locally as a Desktop application):

    mvn clean package

This will produce a jar called `*-jar-with-dependencies.jar` under `target`.
You can either click on the jar to run it or, from the command line, run
    `java -jar target/*-jar-with-dependencies.jar [<OPACITY>]`

(By specifying a number in the range `[0.0, 1.0]`, you can adjust the opacity of the window. ;) )


Applet
--------

To build an applet `jar` to run in a browser

    mvn clean package
	
This will build the `jar` and also sign it. (The certificate is only valid for six months!)
See the simple <a href="https://github.com/oontvoo/card-tricks/blob/master/index.html">`index.html`</a> for example of how to use the applet jar.



Note: If you want, you can also create your own keystore by running something like this:

    keytool -genkey -alias applet -keyalg RSA -keystore src/main/keystore/signing-jar.keystore -storepass applet -keypass applet -dname "CN=domain"
