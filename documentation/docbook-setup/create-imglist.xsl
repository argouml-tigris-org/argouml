<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:saxon="http://icl.com/saxon"
	xmlns:lxslt="http://xml.apache.org/xslt"
	xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
	exclude-result-prefixes="doc"
	extension-element-prefixes="saxon xalanredirect lxslt">

	<xsl:import href="docbook-xsl-1.49/html/docbook.xsl"/>

<xsl:template match="/">
  <xsl:call-template name="dumplist"/>
</xsl:template>

<xsl:template match="text()">
</xsl:template>

<xsl:template name="dumplist">
  <xsl:call-template name="write.chunk">
    <xsl:with-param name="filename" select="'imglist.txt'"/>
    <xsl:with-param name="content">
      <xsl:apply-templates/>
    </xsl:with-param>
  </xsl:call-template>
</xsl:template>

<xsl:template match="imagedata|inlinegraphic">
  <xsl:message>
    <xsl:text>Found image file: </xsl:text>
    <xsl:value-of select="@fileref"/>
  </xsl:message>
  <xsl:value-of select="@fileref"/>
  <xsl:text>
</xsl:text>
</xsl:template>

<xsl:template match="note|caution|important|tip|warning">
  <xsl:message>
    <xsl:text>Found </xsl:text>
    <xsl:value-of select="name(.)"/>
  </xsl:message>
  <xsl:text>images/</xsl:text>
  <xsl:value-of select="name(.)"/>
  <xsl:text>.png
</xsl:text>
</xsl:template>

<xsl:template match="callout">
  <xsl:message>
    <xsl:text>Found </xsl:text>
    <xsl:value-of select="name(.)"/>
  </xsl:message>
  <xsl:text>images/callouts/*.png
</xsl:text>
</xsl:template>



<xsl:template match="*">
  <xsl:apply-templates/>
</xsl:template>

</xsl:stylesheet>

