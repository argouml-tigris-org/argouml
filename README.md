# argouml
Main project of argouml.

Started in January 1998. Converted from CVS to Subversion in 2006. Converted to git in 2019.

## Resources

* Github organisation: <https://github.com/argouml-tigris-org>
* Web page with maven sites: <https://argouml-tigris-org.github.io/>

## Contributing

Short summary on how to contribute to the project using github and gerrithub:

* One pull request per issue or per change;
* Pull requests towards the master branch;
* No merge commits;
* Clean up "fix" commits before creating the pull request;
* Push changes towards master to gerrithub.
* One change per issue.
* Code clean-up in separate changes (gerrithub will make each commit into a change).
* Use the repo tool.

A longer explaination is in <https://github.com/argouml-tigris-org/argouml/wiki/Working-in-the-project>

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
