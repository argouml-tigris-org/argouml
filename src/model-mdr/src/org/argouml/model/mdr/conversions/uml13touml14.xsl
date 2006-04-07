<?xml version="1.0" encoding="utf-8" ?>
<!-- $Id$
// Copyright (c) 2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.
	
    Converter from UML 1.3/XMI 1.0 to UML 1.4/XMI1.2
    
    This has only been tested using the output from 
    ArgoUML's NSUML implementation, as cleaned by
    the NormalizeNSUML.xsl style sheet.

    Authors: Ludovic Maitre and Tom Morris

    The OMG document ad/01-02-24 available at 
    http://www.omg.org/docs/ad/01-02-24.htm
    summarizes the differences between UML 1.3 and UML 1.4
-->
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    xmlns:xs="http://www.w3.org/2001/XMLSchema"
    xmlns:xalan="http://xml.apache.org/xalan"
    xmlns:saxon = "http://icl.com/saxon"
    xmlns:UML="org.omg.xmi.namespace.UML"
    xmlns:date="http://xml.apache.org/xalan/java/java.util.Date"
    xmlns:java_lang="http://xml.apache.org/xalan/java/java.lang"
    xmlns:fn="http://www.w3.org/2005/02/xpath-functions"
    extension-element-prefixes="fn date java_lang"
    exclude-result-prefixes = "xs fn date xalan saxon java_lang"	
>
  <xsl:output method="xml" indent="yes" encoding="utf-8" 
         xalan:indent-amount="2" saxon:indent-spaces="2"/>
  <xsl:variable name="version" select="0.4"/>
  <xsl:variable name="now" select="date:new()"/>

  <xsl:key name="stereo-key" match="Foundation.Extension_Mechanisms.Stereotype" 
    use="./Foundation.Extension_Mechanisms.Stereotype.extendedElement/Foundation.Core.ModelElement/@xmi.idref"/>
  
  <!-- Copy xmi.uuid to xmi.id to support GEF/PGML references -->
  <xsl:include href="UuidToXmiId.xsl"/>
  
  <xsl:template match="/">
    <xsl:text>&#xa;</xsl:text>
    <xsl:comment>
      Converted from UML 1.3 to UML 1.4 on: <xsl:value-of select="date:toString($now)"/>
      Converter version <xsl:value-of select="$version"/> by Ludovic Maitre and Tom Morris
</xsl:comment>
    <xsl:text>&#xa;</xsl:text>
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>

  <xsl:template match="/XMI">
    <XMI xmi.version="1.2" xmlns:UML="org.omg.xmi.namespace.UML">
      <xsl:attribute name="timestamp">
        <xsl:value-of select="date:toString($now)"/>
      </xsl:attribute>
      <xsl:apply-templates />
    </XMI>
  </xsl:template>

  <xsl:template name="removed-from-uml14"/>

  <xsl:template match="*">
    <xsl:choose>
      <xsl:when test="name()='XMI.metamodel'"/>
	<!-- TODO: preserve old XMI exporter info in comments at a minimum -->
	<xsl:when test="name()='XMI.exporter'"><XMI.exporter>UML 1.3 to UML 1.4 stylesheets</XMI.exporter></xsl:when>
	<xsl:when test="name()='XMI.exporterVersion'"><XMI.exporterVersion><xsl:value-of select="$version"/> </XMI.exporterVersion></xsl:when>
      <xsl:when test="starts-with(name(.),'XMI')">
        <xsl:copy>
          <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
      </xsl:when>
      <xsl:otherwise>
        <xsl:call-template name="UML"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="@*">
    <xsl:copy>
      <xsl:apply-templates select="@*"/>
    </xsl:copy>
  </xsl:template>
  
  <!-- delete all elements which are no longer supported in UML 1.4 -->
  <!-- many types of back links were removed from UML 1.4  -->
  <xsl:template match="Foundation.Core.ModelElement.elementResidence"/>
  <xsl:template match="Foundation.Core.ModelElement.presentation"/>
  <xsl:template match="Foundation.Core.ModelElement.supplierDependency"/>
  <xsl:template match="Foundation.Core.ModelElement.templateParameter2"/>
  <xsl:template match="Foundation.Core.ModelElement.templateParameter3"/>
  <xsl:template match="Foundation.Core.ModelElement.binding"/>
  <xsl:template match="Foundation.Core.GeneralizableElement.specialization"/>
  <xsl:template match="Foundation.Core.Classifier.associationEnd"/>
  <xsl:template match="Foundation.Core.Classifier.participant"/>
  <xsl:template match="Foundation.Core.Operation.method"/>
  <xsl:template match="Foundation.Extension_Mechanisms.Stereotype.extendedElement"/>
  <xsl:template match="Foundation.Extension_Mechanisms.Stereotype.requiredTag"/>
  <xsl:template match="Foundation.Extension_Mechanisms.TaggedValue.stereotype"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Signal.context"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Signal.reception"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Signal.sendAction"/>
  <!--
  <xsl:template match="Behavioral_Elements.Use_Cases.UseCase.include"/>
  -->
  <xsl:template match="Behavioral_Elements.Use_Cases.UseCase.include2"/>
  <xsl:template match="Behavioral_Elements.Use_Cases.UseCase.extend2"/>
  <xsl:template match="Behavioral_Elements.Use_Cases.ExtensionPoint.extend"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Link.stimulus"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Instance.attributeLink"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Action.stimulus"/>
  <xsl:template match="Behavioral_Elements.State_Machines.Event.state"/>
  <xsl:template match="Behavioral_Elements.State_Machines.Event.transition"/>
  <xsl:template match="Behavioral_Elements.State_Machines.Transition.state"/>
  
  <!-- anything followed by a number is typically a reverse link which is no longer supported in UML 1.4 -->
  <xsl:template match="Behavioral_Elements.Collaborations.ClassifierRole.message1"/>
  <xsl:template match="Behavioral_Elements.Collaborations.ClassifierRole.message2"/>
  <xsl:template match="Behavioral_Elements.Collaborations.Message.message3"/>
  <xsl:template match="Behavioral_Elements.Collaborations.Message.message4"/>
  
    <!-- Additional elements removed not mentioned in OMG document -->
  <xsl:template match="Behavioral_Elements.Common_Behavior.Action.state1"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Action.state2"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Action.state3"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Instance.stimulus1"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Instance.stimulus2"/>
  <xsl:template match="Behavioral_Elements.Common_Behavior.Instance.stimulus3"/>

  <!-- Extension points need to be nested in UseCase for UML 1.4  -->
  <!-- Copy ExtensionPoint into the place it's referenced from -->
  <xsl:key name="xmi.id-key" match="*" use="@xmi.id"/>
  <xsl:template match="Behavioral_Elements.Use_Cases.UseCase.extensionPoint/Behavioral_Elements.Use_Cases.ExtensionPoint[@xmi.idref]"
      priority="0.9">
      <xsl:for-each select="key('xmi.id-key',@xmi.idref)">
        <xsl:call-template name="UML"/>
      </xsl:for-each>
  </xsl:template>
  <!-- delete the original copy  (do this last) -->
  <!-- we probably want to delete anything in the wrong place, but this only takes care of where NSUML puts them -->
  <xsl:template match="Foundation.Core.Namespace.ownedElement/Behavioral_Elements.Use_Cases.ExtensionPoint" >
  </xsl:template>
  <!-- delete illegal nested elements first --> 
  <xsl:template match="Behavioral_Elements.Use_Cases.ExtensionPoint.useCase" priority="1">
  </xsl:template>
  <xsl:template match="Behavioral_Elements.Use_Cases.ExtensionPoint/Foundation.Core.ModelElement.namespace" priority="1" >
  </xsl:template>
  
  <!-- Rewrite UML 1.3 TaggedValue.tag in the UML 1.4 style -->
  <!-- tag type defaults to String? -->
  <!-- careful with generated IDs - they can collide with NormalizeNSUML.xsl -->
  <xsl:variable name="tagdefinitions">
    <xsl:for-each
        select="//Foundation.Extension_Mechanisms.TaggedValue.tag[not(text()=preceding::text())]">
      <xsl:sort select="text()"/>
      <xsl:call-template name="write-tag">
        <xsl:with-param name="tag" select="." />
      </xsl:call-template>
    </xsl:for-each>
  </xsl:variable>

  <xsl:template name="write-tag">
    <xsl:param name="tag" />
    <UML:TagDefinition xmi.id="{generate-id()}_tagdef" 
        name="{$tag}">
      <UML:TagDefinition.multiplicity>
        <UML:Multiplicity xmi.id="{generate-id()}_tagmult">
          <UML:Multiplicity.range>
            <UML:MultiplicityRange 
                xmi.id="{generate-id()}_tagmultrange" lower='1' upper='1' />
          </UML:Multiplicity.range>
        </UML:Multiplicity>
      </UML:TagDefinition.multiplicity>
    </UML:TagDefinition>
  </xsl:template>
  
  <xsl:template name="write-tag-idref">
    <xsl:param name="tag" />
    <UML:TaggedValue.type>
      <UML:TagDefinition>
        <xsl:attribute name="xmi.idref">
          <xsl:value-of 
              select="xalan:nodeset($tagdefinitions)//*[@name=$tag]/@xmi.id" />
        </xsl:attribute>
      </UML:TagDefinition>
    </UML:TaggedValue.type>
  </xsl:template>
	
  <!-- 
    This is where all the main work is done
  -->
  <xsl:template name="UML">
    <xsl:variable name="umlName">
      <xsl:call-template name="lastName">
        <xsl:with-param name="aString" select="name()"/>
      </xsl:call-template>
    </xsl:variable>
    <xsl:variable name="fatherBase" select="substring-before(name(),$umlName)"/>
    <xsl:variable name="fatherName">
      <xsl:call-template name="lastName">
        <xsl:with-param name="aString" select="substring($fatherBase,1,string-length($fatherBase)-1)"/>
      </xsl:call-template>
    </xsl:variable>
<!-- special code for unexpected conditions needs more testing
    <xsl:variable name="qualifiedName">
      <xsl:choose>
        <xsl:when test="$fatherName = ''">
          <xsl:value-of select="$umlName"/>
        </xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="concat($fatherName,'.',$umlName)"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
-->
    <!-- line below to be replaced with above when better tested -->
    <xsl:variable name="qualifiedName" select="concat($fatherName,'.',$umlName)"/>
    <xsl:variable name="fullName">
      <xsl:choose>
        <!-- State has become abstract in UML 1.4 - map it to SimpleState -->
        <xsl:when test="$umlName = 'State'">SimpleState</xsl:when>
        <!-- if first character is upper case it's a UML element -->
        <xsl:when test="contains('ABCDEFGHIJKLMNOPQRSTUVWXYZ',substring($umlName,1,1))">
          <xsl:value-of select="$umlName"/>
        </xsl:when>
        <xsl:when test="$qualifiedName = 'ModelElement.supplierDependency'">ModelElement.clientDependency</xsl:when>
        <!-- TaggedValues -->
        <xsl:when test="$qualifiedName = 'TaggedValue.value'">TaggedValue.dataValue</xsl:when>
        <xsl:when test="$qualifiedName = 'TaggedValue.tag'">TaggedValue.type</xsl:when>
        <!-- Elements to be renamed from OMG document -->
        <xsl:when test="$qualifiedName = 'ModelElement.templateParameter2'">ModelElement.parameterTemplate</xsl:when>
        <xsl:when test="$qualifiedName = 'ModelElement.templateParameter3'">ModelElement.defaultedParameter</xsl:when>
        <xsl:when test="$qualifiedName = 'Classifier.structuralFeature'">Classifier.typedFeature</xsl:when>
        <xsl:when test="$qualifiedName = 'Classifier.parameter'">Classifier.typedParameter</xsl:when>
        <xsl:when test="$qualifiedName = 'AssociationEnd.type'">AssociationEnd.participant</xsl:when>
        <xsl:when test="$qualifiedName = 'Node.resident'">Node.deployedComponent</xsl:when>
        <xsl:when test="$qualifiedName = 'ElementResidence.implementationLocation'">ElementResidence.container</xsl:when>
        <xsl:when test="$qualifiedName = 'TemplateParameter.modelElement'">TemplateParameter.template</xsl:when>
        <xsl:when test="$qualifiedName = 'TemplateParameter.modelElement2'">TemplateParameter.parameter</xsl:when>
        <xsl:when test="$qualifiedName = 'Constraint.constrainedElement2'">Constraint.constrainedStereotype</xsl:when>
        <xsl:when test="$qualifiedName = 'UseCase.include2'">UseCase.include</xsl:when>
        <xsl:when test="$qualifiedName = 'StateMachine.subMachineState'">StateMachine.submachineState</xsl:when>
        <!-- unclear whether these were renamed or deleted - 
	     they are listed both places in OMG - I think deleted
        <xsl:when test="$qualifiedName = 'ClassifierRole.message1'">ClassifierRole.message</xsl:when>
        <xsl:when test="$qualifiedName = 'ClassifierRole.message2'">ClassifierRole.message</xsl:when>
        <xsl:when test="$qualifiedName = 'Message.message3'">Message.successor</xsl:when>
        <xsl:when test="$qualifiedName = 'Message.message4'">Message.message</xsl:when> 
        -->
        <xsl:when test="$qualifiedName = 'ElementImport.modelElement'">ElementImport.importedElement</xsl:when>
        <xsl:otherwise>
          <xsl:value-of select="$fatherName"/>.<xsl:value-of select="$umlName"/>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:variable>
    <xsl:call-template name="build-uml">
      <xsl:with-param name="umlName" select="$fullName"/>
    </xsl:call-template>
  </xsl:template>


  <xsl:template name="build-uml">
    <xsl:param name="umlName"/> 
    <xsl:choose>
      <xsl:when test="$umlName = ''"></xsl:when>
      <xsl:when test="$umlName = 'TaggedValue.type'">
        <xsl:call-template name="write-tag-idref">
          <xsl:with-param name="tag" select="text()"/>
        </xsl:call-template>  
      </xsl:when>
      <xsl:otherwise>
      <xsl:element name="UML:{$umlName}">
      <xsl:if test="$umlName = 'Namespace.ownedElement' and name(..) = 'Model_Management.Model'">
	<xsl:copy-of select="$tagdefinitions" /> 
      </xsl:if>         
      <!-- Map child elements with attribute of xmi.value to an attribute on the parent -->
        <xsl:for-each select="*[@xmi.value]">
          <xsl:variable name="attribName">
            <xsl:call-template name="lastName">
              <xsl:with-param name="aString" select="name()"/>
            </xsl:call-template>
          </xsl:variable>
          <xsl:attribute name="{$attribName}">
            <xsl:value-of select="@xmi.value"/>
          </xsl:attribute>
	  <xsl:variable name="attribValue" select="@xmi.value"/>
	  <xsl:choose>
	    <xsl:when test="$umlName = 'Pseudostate' 
                and $attribName = 'kind' and $attribValue = 'branch'">
	      <xsl:attribute name="{$attribName}">choice</xsl:attribute>
            </xsl:when>
	    <xsl:otherwise>
	      <xsl:attribute name="{$attribName}"><xsl:value-of select="@xmi.value"/></xsl:attribute>
	    </xsl:otherwise>
	  </xsl:choose>
        </xsl:for-each>
      <!-- Map child name element to name attribute on parent unless
           its the name of a comment in which case it moves to body -->
        <xsl:if test="*[contains(substring(name(.),string-length(name(.))-4),'.name')]">
          <xsl:choose>
            <xsl:when test="$umlName = 'Comment'">
              <xsl:attribute name="body">
                <xsl:value-of select="*[contains(substring(name(.),string-length(name(.))-4),'.name')]"/>
              </xsl:attribute>
            </xsl:when>
            <xsl:otherwise>
              <xsl:attribute name="name">
                <xsl:value-of select="*[contains(substring(name(.),string-length(name(.))-4),'.name')]"/>
              </xsl:attribute>
            </xsl:otherwise>
	  </xsl:choose>
        </xsl:if>
      <!-- Create reference to stereotype if there's one with our ID  -->
        <!-- try xmi.uuid first (Argo specific), then use xmi.id if not present -->
        <xsl:choose>
          <xsl:when test="key('stereo-key',@xmi.id)/@xmi.uuid">
            <xsl:attribute name="stereotype">
              <xsl:value-of select="key('stereo-key',@xmi.id)/@xmi.uuid"/>
            </xsl:attribute>
          </xsl:when>
          <xsl:when test="key('stereo-key',@xmi.id)/@xmi.id">
            <xsl:attribute name="stereotype">
              <xsl:value-of select="key('stereo-key',@xmi.id)/@xmi.id"/>
            </xsl:attribute>
          </xsl:when>
          <!-- otherwise do nothing -->
        </xsl:choose>

      <!-- Copy all other attributes unchanged -->
        <xsl:apply-templates select="@*"/>
        <xsl:value-of select="text()"/>
        <xsl:for-each select="*[not(@xmi.value) and not(contains(substring(name(.),string-length(name(.))-4),'.name'))]">
          <xsl:apply-templates select="."/>
        </xsl:for-each>
      </xsl:element>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- Recursive function to return rightmost dot separated element of a name -->
  <xsl:template name="lastName">
    <xsl:param name="aString"/>
    <xsl:choose>
      <xsl:when test="contains($aString,'.')">
        <xsl:call-template name="lastName">
          <xsl:with-param name="aString" select="substring-after($aString, '.')"/>
        </xsl:call-template>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$aString"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>
