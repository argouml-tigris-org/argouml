This directory contains UML profiles for use with ArgoUML.


PROFILES
- default-uml14.xmi - standard model elements from UML 1.4 spec
- default-uml14-uml20-deprecated.xmi - standard model elements from
       UML 1.4 spec, but with items deleted from UML 2.0 marked as
       deprecated.
- default-uml14-uml20-subset.xmi - subset of standard model elements
       from UML 1.4 spec which are also in UML 2.0 spec.

- andromda-profile-31.xmi - Modeling profile for standard AndroMDA 3.1 cartridges
- andromda-profile-32-noextensions.xmi - Modeling profile for standard 
       AndroMDA 3.2 cartridges.  Created by taking distributed AndroMDA 3.2
       profile, importing all MagicDraw "modules", and writing out as "rich"
       XMI file which was then imported into ArgoUML and written back out again
       to strip all MagicDraw extensions.