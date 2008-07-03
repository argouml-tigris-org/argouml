The following files:

	versioninfo.txt  date of build
	licenses.txt     licenses - see clarification below
	jmi.jar          Java Metadata Interfaces - license in jmi-license.txt
                     from http://jcp.org/aboutJava/communityprocess/final/jsr040/index.html
	jmiutils.jar	
	mdrapi.jar	
	mof.jar          MOF interfaces generated using JMI on OMG MOF definition
                     license in mof-license.txt
	nbmdr.jar	
	openide-util.jar
	
are from the NetBeans project and were downloaded from

    http://mdr.netbeans.org/download/daily.html

which is described as 

mdr-standalone.zip  Daily updated zip archive containing all the jars 
                    needed to use MDR without NetBeans.

The license in licenses.txt is a little confusing, so the MDR project leader, 
Sun employee Martin Matula, provided the following clarification (archived
at http://mdr.netbeans.org/servlets/ReadMsg?list=users&msgNo=3338) for 
the three licenses contained in licenses.txt:

------
Date: Tue, 11 Oct 2005 20:03:04 +0200
From: Martin Matula <Martin.Matula@Sun.COM>
Content-type: text/plain; charset=ISO-8859-1; format=flowed
Subject: [mdr-users] License terms for mdr-standalone.zip ?

Oh, I see. This is Sun's SLA. MDR itself is under SPL, but it uses some 
3rd party libraries (such as the jar with JMI interfaces) which are 
licensed under a different license. SLA is a generic license which is 
modified for a particular product by the supplemental terms (in this 
case the supplemental terms for JMI interfaces). So, supplemental 
license terms take the precedence - they are included in the license.txt 
following this SLA.
Martin
-------

Based on this statement and an examination of the MDR build procedure which
uses the actual license text to descramble third party libraries, the
libaries licensed under the Sun Public License include: jmiutils.jar,
mdrapi.jar, nbmdr.jar, openide-util.jar.  The file jmi.jar is licensed under
the license in jmi-license.txt.  The file mof.jar is licensed under
the license in mof-license.txt.