<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:saxon="http://icl.com/saxon"
	xmlns:lxslt="http://xml.apache.org/xslt"
	xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
        xmlns:exsl="http://exslt.org/common"
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
	exclude-result-prefixes="doc"
	extension-element-prefixes="saxon xalanredirect lxslt">

<xsl:param name="output.method" select="'text'"/>
<xsl:param name="default.encoding" select="'ISO-8859-1'"/>
<xsl:param name="saxon.character.representation" select="'entity;decimal'"/>

<xsl:template name="write.chunk">
  <xsl:param name="filename" select="''"/>
  <xsl:param name="method" select="$output.method"/>
  <xsl:param name="encoding" select="$default.encoding"/>
  <xsl:param name="indent" select="'no'"/>
  <xsl:param name="content" select="''"/>

  <xsl:choose>
    <xsl:when test="element-available('exsl:document')">
      <exsl:document href="{$filename}"
                     method="{$method}"
                     encoding="{$encoding}"
                     indent="{$indent}">
        <xsl:copy-of select="$content"/>
      </exsl:document>
    </xsl:when>
    <xsl:when test="element-available('saxon:output')">
      <saxon:output href="{$filename}"
                    method="{$method}"
                    encoding="{$encoding}"
                    indent="{$indent}"
                    saxon:character-representation="{$saxon.character.representation}">
        <xsl:copy-of select="$content"/>
      </saxon:output>
    </xsl:when>
    <xsl:when test="element-available('xalanredirect:write')">
      <!-- Xalan uses xalanredirect -->
      <xalanredirect:write file="{$filename}">
        <xsl:copy-of select="$content"/>
      </xalanredirect:write>
    </xsl:when>
    <xsl:otherwise>
      <!-- it doesn't matter since we won't be making chunks... -->
      <xsl:message terminate="yes">
        <xsl:text>Can't make chunks with </xsl:text>
        <xsl:value-of select="system-property('xsl:vendor')"/>
        <xsl:text>'s processor.</xsl:text>
      </xsl:message>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>


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

