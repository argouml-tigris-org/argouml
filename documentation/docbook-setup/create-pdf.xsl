<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:saxon="http://icl.com/saxon"
	xmlns:lxslt="http://xml.apache.org/xslt"
	xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
	exclude-result-prefixes="doc"
	extension-element-prefixes="saxon xalanredirect lxslt">

	<xsl:import href="docbook-xsl-1.60.1/fo/docbook.xsl"/>
	<xsl:import href="commonsettings.xsl"/>

        <!-- Added by Jeremy Bennett for 1.49 XSL stylesheets -->

        <xsl:variable name="fop.extensions" select="1"/>
        <xsl:variable name="stylesheet.result.type" select="fo"/>
        <xsl:variable name="paper.type" select="A4"/>
        <xsl:variable name="default.table.width" select="'15cm'"/>
        <xsl:variable name="draft.watermark.image"
                      select="docbook-xsl-1.60.1/images/draft.png"/>

</xsl:stylesheet>
