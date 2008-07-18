<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:Model='omg.org/mof.Model/1.3'>

<xsl:output method="xml" indent="yes"/>	

<xsl:template match="/XMI/XMI.content/Model:Package[@name='Core']/Model:Namespace.contents">
	<panels>
     	<xsl:for-each select="Model:Class">
			<panel name="{@name}">
			<xsl:for-each select="Model:Namespace.contents//Model:Attribute">
		  		<xsl:variable name="datatypeRef" select="@type" />
  				<xsl:variable name="datatype" select="//Model:DataType[@xmi.id=$datatypeRef]/@name" />
		  		<item name="{@name}" type="{$datatype}" />   		
			</xsl:for-each>
			</panel>
      	</xsl:for-each>
	</panels>
   	<xsl:apply-templates />
</xsl:template>
	
<xsl:template match="@*|node()">
   	<xsl:apply-templates/>
</xsl:template>

</xsl:stylesheet>