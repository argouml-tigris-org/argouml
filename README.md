# argouml
Main project of argouml.

Started in January 1998. Converted from CVS to Subversion in 2006. Converted to git in 2019.

## Resources

* Github organisation: <https://github.com/argouml-tigris-org>
* Web page with maven sites: <https://argouml-tigris-org.github.io/>

## Contributing

Short summary on how to contribute to the project using github and gerrithub:

* Push changes towards master to gerrithub.
* One change per issue.
* Code clean-up in separate changes (gerrithub will make each commit into a change).
* Use the repo tool.

A longer explaination is in <https://github.com/argouml-tigris-org/argouml/wiki/Working-in-the-project>

## Build instruction

First, building ArgoUML requires some setup on the build machine.
Some minimal requirements:

* Sources must have been extracted by using Gerrit repo tool
* You will need [Maven](https://maven.apache.org/), a version that is known to work is maven 3.8.5,
* You will need a Java compiler at least some JDK 1.8;  it is known to compile and work with OpenJDK 11

Extract the sources with `repo` so that you will get all the components that ArgoUML is using:

```
mkdir argouml
cd argouml
repo init -u git@github.com:argouml-tigris-org/manifest.git
repo sync
```

You'll see a `.repo` hidden directory as well as several `argouml-*` directories.
To build ArgoUML use (you should be in `<DIR>/argouml/argouml`):

```
cd argouml
mvn package
```

To build without running the Unit tests, you can use the command:

```
mvn package -DskipTests=true
```

At the end, maven will print the path of the JAR that contains everything.
The path should look like `argouml/src/argouml-build/target/argouml-jar-with-dependencies.jar`.
