<?xml version="1.0" encoding="utf-8"?>
<!--
	Author: Ludovic Maitre
	File: DIRemove.xsl
	Date: 08/08/2005
	Purpose: Remove the Diagram Interchange Extensions of an XMI file
-->
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xs="http://www.w3.org/2001/XMLSchema"
	xmlns:UML="org.omg.xmi.namespace.UML"
>
	<xsl:output method="xml" indent="yes" encoding="utf-8" />
	<xsl:strip-space elements="*" />
	
	<xsl:template match="/">
		<xsl:apply-templates />
	</xsl:template>
	
	<xsl:template match="XMI.content">
	<XMI.content>
		<xsl:apply-templates select="UML:Model"/>
	</XMI.content>
	</xsl:template>
	
<xsl:template match="@*|node()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()"/>
  </xsl:copy>
</xsl:template>
	
</xsl:stylesheet>