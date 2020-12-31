# argouml
Main project of argouml.

Started in January 1998. Converted to Subversion in 2006. Converted to git in 2019.

## Resources

* Homepage: <http://argouml.tigris.org>
* Using ArgoUML: <http://argouml.tigris.org/documentation/>
* Developer Wiki: <http://argouml.tigris.org/wiki>
* Issues: <http://argouml.tigris.org/issues/query.cgi>

## Contributing

Short summary on how to contribute to the project using github:

* One pull request per issue or per change;
* Pull requests towards the master branch;
* No merge commits;
* Clean up "fix" commits before creating the pull request;

### Setup ArgoUML

* clone the ArgoUML repository:
  `git clone https://github.com/argouml-tigris-org/argouml.git argouml`
  `cd argouml`

* in the argouml directory setup local ANT environment:
  `export ANT_NOME=<path to argouml>/arguml/tools/apache-ant-1.7.0`

* build ArgoUML:
  `toos/apache-ant-1.7.0/bin/ant install`

* install ArgoUML locally:
  `mv build argouml-0.35.1`  (use the up to date version)

  `sudo mv argouml-0.35.1 /opt`
  `sudo ln -s /opt/argouml-0.35.1/argouml.sh /usr/bin/argouml`

* create a local **argouml.desktop** file with the contents

  ```
  [Desktop Entry]
  Encoding=UTF-8
  Name=Argo UML
  Comment=Argo UML
  Exec=/usr/bin/argouml
  Icon=/opt/argouml-0.35.1/ArgoUML.ico
  Categories=Application;Development;;ArgoUML;IDE
  Version=1.0
  Type=Application
  Terminal=0
  ```

  `sudo mv argouml.desktop /usr/share/applications`
