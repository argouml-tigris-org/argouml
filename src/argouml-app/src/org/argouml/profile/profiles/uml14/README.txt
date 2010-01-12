This directory contains the default UML profiles distributed with ArgoUML.  They
are not accessed over the web for ArgoUML usage, but are included here so that
they are available for other tools which consume XMI files exported from 
ArgoUML.

Because the linkages between the XMI files are made by ID, the links are
impervious to name changes, but the IDs must be kept stable.  For this reason
if an element is ever deleted or has its ID changed, a new version of the
profile file will be created so that the old version is available for existing
projects to continue to access.

STANDARD PROFILES

- default-uml14.xmi - standard model elements from Appendix A of the 
       UML 1.4 specification

- default-java.xmi - Java-specific types and datatypes.  Prior to ArgoUML 0.26,
       the default profile was a combination of this profile plus 
       the UML 1.4 profile.  It currently only contains a small set of types
       from java.lang, java.util, and java.math.

- metaprofile.xmi - contains the elements needed to create profiles with ArgoUML.

- C++ profile - where?

CONTRIBUTED AND OTHER PROFILES

- default-uml14-uml20-deprecated.xmi - same as default-uml14, but with items
       which have been removed in the UML 2.0 specification marked as
       deprecated.

- default-uml14-uml20-subset.xmi - subset of standard model elements
       from UML 1.4 specifcation which are also in UML 2.0.
       
- andromda-profile-32-noextensions.xmi - Modeling profile for standard 
       AndroMDA 3.2 cartridges.  Created by taking distributed AndroMDA 3.2
       profile, importing all MagicDraw "modules", and writing out as "rich"
       XMI file which was then imported into ArgoUML and written back out again
       to strip all MagicDraw extensions.

- andromda-profile-31.xmi - Modeling profile for standard AndroMDA 3.1 cartridges
       
