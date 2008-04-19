<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  $Id$
  Convert Microsoft Visio XMI file produced by Visio XMI addin 
  to a form that can be imported by ArgoUML (UML 1.4).
  Originally started to address ArgoUML issue 4147.
  
  ** INCOMPLETE & UNTESTED ** - use as starting point only
  
  Geometry - remove Geometry & Geometry.body
  Shared aggregation - convert to aggregate
  Multiplicity - change string encoding to UML encoding
  Dependencies - remove ModelElement.provision/requirement elements
  Generalizations - change subtype to child and supertype to parent
  
  Author: Tom Morris tfmorris@gmail.com
  Created: 20060406
  
-->
<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

    <xsl:output method="xml" indent="yes" encoding="UTF-8" />

    <xsl:template match="/">
        <xsl:apply-templates />
    </xsl:template>

    <!-- Remove unsupported elements -->
    <xsl:template match="Foundation.Data_Types.Geometry"/>
    <xsl:template match="Foundation.Core.Classifier.associationEnd"/>
    <xsl:template match="Foundation.Core.ModelElement.provision"/>
    <xsl:template match="Foundation.Core.ModelElement.requirement"/>
    
    <!-- TODO: Handle Multiplicity string to XML mapping -->
    <!-- Strings seen: 1, *, 1..*, empty string, punt on others? -->
    <xsl:template match="Foundation.Core.StructuralFeature.multiplicity">
      <xsl:variable name="multi" select="text()"/>
      <xsl:variable name="lower">
        <xsl:choose>
          <xsl:when test="$multi = '*'">0</xsl:when>
          <xsl:when test="$multi = '1'">1</xsl:when>
          <xsl:when test="$multi = '1..*'">1</xsl:when>
          <xsl:otherwise/>
	</xsl:choose>
      </xsl:variable>
      <xsl:variable name="upper">
        <xsl:choose>
          <xsl:when test="$multi = '*'">-1</xsl:when>
          <xsl:when test="$multi = '1'">1</xsl:when>
          <xsl:when test="$multi = '1..*'">-1</xsl:when>
          <xsl:when test="$multi = ''">0</xsl:when>
          <xsl:otherwise>0<xsl:message>Unknown multiplicity: <xsl:value-of select="$multi"/></xsl:message>
	  </xsl:otherwise>
	</xsl:choose>
      </xsl:variable>
      
      <xsl:if test="$upper != '0'">
        <Foundation.Core.StructuralFeature.multiplicity>
          <Foundation.Data_Types.Multiplicity>
            <Foundation.Data_Types.Multiplicity.range>
              <Foundation.Data_Types.MultiplicityRange>
		  <xsl:attribute name="lower">
		    <xsl:value-of select="$lower"/>
		  </xsl:attribute>
		  <xsl:attribute name="upper">
		    <xsl:value-of select="$upper"/>
		  </xsl:attribute>
	       </Foundation.Data_Types.MultiplicityRange>
            </Foundation.Data_Types.Multiplicity.range>
          </Foundation.Data_Types.Multiplicity>
        </Foundation.Core.StructuralFeature.multiplicity>
      </xsl:if>
    </xsl:template>
    
    <!-- TODO: Paramaterize this duplicate code -->
    <xsl:template match="Foundation.Core.AssociationEnd.multiplicity">
      <xsl:variable name="multi" select="text()"/>
      <xsl:variable name="lower">
        <xsl:choose>
          <xsl:when test="$multi = '*'">0</xsl:when>
          <xsl:when test="$multi = '1'">1</xsl:when>
          <xsl:when test="$multi = '1..*'">1</xsl:when>
          <xsl:otherwise/>
	</xsl:choose>
      </xsl:variable>
      <xsl:variable name="upper">
        <xsl:choose>
          <xsl:when test="$multi = '*'">-1</xsl:when>
          <xsl:when test="$multi = '1'">1</xsl:when>
          <xsl:when test="$multi = '1..*'">-1</xsl:when>
          <xsl:when test="$multi = ''">0</xsl:when>
          <xsl:otherwise>0<xsl:message>Unknown multiplicity: <xsl:value-of select="$multi"/></xsl:message>
	  </xsl:otherwise>
	</xsl:choose>
      </xsl:variable>
      
      <xsl:if test="$upper != '0'">
        <Foundation.Core.AssociationEnd.multiplicity>
          <Foundation.Data_Types.Multiplicity>
            <Foundation.Data_Types.Multiplicity.range>
              <Foundation.Data_Types.MultiplicityRange>
		  <xsl:attribute name="lower">
		    <xsl:value-of select="$lower"/>
		  </xsl:attribute>
		  <xsl:attribute name="upper">
		    <xsl:value-of select="$upper"/>
		  </xsl:attribute>
	       </Foundation.Data_Types.MultiplicityRange>
            </Foundation.Data_Types.Multiplicity.range>
          </Foundation.Data_Types.Multiplicity>
        </Foundation.Core.AssociationEnd.multiplicity>
      </xsl:if>
    </xsl:template>
    
    <!-- Change 'shared' aggregation to 'aggregate' -->
    <xsl:template match="Foundation.Core.AssociationEnd.aggregation[@xmi.value='shared']">
      <Foundation.Core.AssociationEnd.aggregation xmi.value="aggregate"/>
    </xsl:template>
    
    <!-- Convert Generalization.subtype/supertype to .child/parent -->
    <xsl:template match="Foundation.Core.Generalization.subtype">
      <Foundation.Core.Generalization.child>
	<xsl:copy-of select="@*|node()"/>
      </Foundation.Core.Generalization.child>
    </xsl:template>
    <xsl:template match="Foundation.Core.Generalization.supertype">
      <Foundation.Core.Generalization.parent>
	<xsl:copy-of select="@*|node()"/>
      </Foundation.Core.Generalization.parent>
    </xsl:template>
    
    <!-- TODO: Convert description to comment? or is it always empty? -->
    <!-- just remove for now -->
    <xsl:template match="Foundation.Core.Dependency.description"/>  

    <!-- Everything else just gets copied directly -->
    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()" />
        </xsl:copy>
    </xsl:template>

</xsl:stylesheet>
