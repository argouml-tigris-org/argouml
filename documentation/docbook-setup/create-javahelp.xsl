<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:saxon="http://icl.com/saxon"
	xmlns:lxslt="http://xml.apache.org/xslt"
	xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
	exclude-result-prefixes="doc"
	extension-element-prefixes="saxon xalanredirect lxslt">

	<xsl:import href="docbook-xsl-1.60.1/javahelp/javahelp.xsl"/>
	<xsl:import href="commonsettings.xsl"/>

	<xsl:variable name="using.chunker" select="1"/>
</xsl:stylesheet>
