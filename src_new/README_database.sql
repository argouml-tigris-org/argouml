This document shall describe how to set up a relational database to use with ArgoUML. This document is maintained by Toby Baier, for any hints please send an email to Toby.Baier@gmx.net.

TESTED SYSTEMS:
So far only MySQL vers. 3.22 is tested on Linux systems. For further successes, please mail to Toby.Baier@gmx.net

WHAT YOU NEED:
You need a working version of ArgoUML (get it from www.argouml.org), higher than 0.8.1. Also you need a relational database system like MySql and an appropriate JDBC driver. The files to configure the database are available at www.argouml.org or shoulb be included in the distribution.

To store your model information in a relational database, there are essentially three steps to be done:
-1- install the database system
-2- set up the UML database
-3- configure ArgoUML to use this database, and even starting the program!

The first will not be discussed here, please refer to the DBMS documentation for this.

############
## Part 2 ##
############

Once you have your DB system runnning, you need to set up the database with its tables. For this I will give examples using the mysql command line tool. To create a database, start the tool 

# mysql -u myDbUserName -p -h myDbServer

log in and enter

mysql> CREATE DATABASE uml;

The name "uml" can be chosen as you like, but I will use "uml" in this documentation. This name is also used in the drop_only.sql and create_only.sql scripts, so if you chose another, yu have to edit these files.
Next you need to set up the tables for this database. While the database is "fresh" and doesn't contain any tables, yu can create all in one swish by leaving the tool and doing

# mysql -u myDbUserName -p -h myDbServer  < create_only.sql

You only have to enter your password, and the tool will do everything for you. You can test it by entering the tool again and typing 

mysql> show tables;

The result of this command should look like: 

-----------------------------------snip---------------------------------
+-------------------------+
| Tables in uml           |
+-------------------------+
| tAbstraction            |
| tAggregationKind        |
| tAssociation            |
| tAssociationEnd         |
| tAttribute              |
| tBehavioralFeature      |
| tBinding                |
| tBoolean                |
| tBooleanExpression      |
| tCallConcurrencyKind    |
| tChangeableKind         |
| tClass                  |
| tClassifier             |
| tConstraint             |
| tDependency             |
| tExpression             |
| tFeature                |
| tGeneralizableElement   |
| tInterface              |
| tMapping                |
| tMessageDirectionKind   |
| tMethod                 |
| tModel                  |
| tModelElement           |
| tMultiplicity           |
| tMultiplicityRange      |
| tNamespace              |
| tOperation              |
| tOrderingKind           |
| tPackage                |
| tParameter              |
| tParameterDirectionKind |
| tPermission             |
| tProcedureExpression    |
| tScopeKind              |
| tStereotype             |
| tStructuralFeature      |
| tTaggedValue            |
| tUsage                  |
| tVisibilityKind         |
+-------------------------+
40 rows in set (0.00 sec) 
-----------------------------------snip---------------------------------


In case you messed up your database, be it with messy data or you destoyed any table structures, you can either use the drop_only.sql to remove all tables from the database.


############
## Part 3 ##
############

Now you are ready to enter stage three of your task: setting up ArgoUML.
You need a file called db.ini somewhere on your system, preferrable in your ArgoUML directory. This is a text file which should look like this:

-----------------------------------snip---------------------------------
[mysql-config]
host=myServer
port=3306
db=uml
user=wigbert
password=secret
driver=org.gjt.mm.mysql.Driver
dbConnectFormat=4                             
-----------------------------------snip---------------------------------

Please replace all entries with the appropriate values. The driver is the classname of your JDBC driver, this example shows the name of the best JDBC driver for MySql, you can download it from their website. The dbConnectFormat depends on the driver used, format 4 is correct for this driver. In case you use a different driver, please try values between 1 and 4. The port number 3306 is the default port for MySql servers.

Now it comes to starting ArgoUML. It is most easy when you put all jars into one directory, and the db.ini file into the same directory. You can either set your CLASSPATH environment variable to find all jars, or use the -cp parameter when you start ArgoUML. Then you need to tell ArgoUML where to find the db.ini file, which you do by passing a -D parameter with the name "argo.dbconfig" and the value of where the db.ini file is located. In case all files are in the same directory, switch to that and enter the following command:

# java -cp argouml09.jar:ocl-argo.jar:nsuml.jar:gef.jar:xerces.jar:mysql_comp.jar -Dargo.dbconfig=db.ini org.argouml.application.Main

If you are using ArgoUML < 0.9 you have to use uci.uml.Main instead of org.argouml.application.Main

USAGE

Now that everything is running, you can simply select "Store model to DB" from the File menu, and the static structurral elements of your model will be stored in the RDBMS, using the name of the top-level model. You should change this from untitledModel to anything else as soon as you want more than one model in the DB.
You can load back your models by choosing "Load model from DB" in the file menu, then you have to enter the name of the model, and it will be there. Please note that only model information is stored, no diagram information. You have to rebuild your diagrams using the right-click "Add to diagram" funtion.

TROUBLESHOOTING

The most frequent problem will be, that the menu entries are "greyed out" and not selectable. That is the case when ArgoUML doesn't find the db.ini file. Please make sure it is at the correct location, readable for your user and passed correctly to the JVM.

If you get an error saying "Couldn't find database driver", please make sure the classname you have as "driver" in your "db.ini" file is in some jar in your classpath.

The error message "Couldn't connect to database" means that either the DBMS is not running, or you have wrong parameters in your db.ini file. It may also be, if you use a remote server over a network, that you have a network error or a firewall which doesn't allow you to make connections over the specified port.
