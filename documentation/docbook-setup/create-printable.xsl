<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:saxon="http://icl.com/saxon"
	xmlns:lxslt="http://xml.apache.org/xslt"
	xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
	exclude-result-prefixes="doc"
	extension-element-prefixes="saxon xalanredirect lxslt">

	<xsl:import href="docbook/html/docbook.xsl"/>
	<xsl:param name="admon.graphics" select="1"/>
	<xsl:param name="admon.graphics.path" select="'images/'"/>
	<xsl:param name="admon.style" select="'margin-left: 0.5in; margin-right: 0.5in;'"/>
	<xsl:param name="author.othername.in.middle" select="1"/>
	<xsl:param name="biblioentry.item.separator" select="'. '"/>
	<xsl:param name="callout.defaultcolumn" select="60"/>
	<xsl:param name="callout.graphics" select="1"/>
	<xsl:param name="callout.graphics.extension" select="'.png'"/>
	<xsl:param name="callout.graphics.number.limit" select="10"/>
	<xsl:param name="callout.graphics.path" select="'images/callouts/'"/>
	<xsl:param name="callout.list.table" select="1"/>
	<xsl:param name="chapter.autolabel" select="1"/>
	<xsl:param name="check.idref" select="1"/>
	<xsl:param name="css.decoration" select="1"/>
	<xsl:param name="default.table.width" select="''"/>
	<xsl:param name="funcsynopsis.decoration" select="1"/>
	<xsl:param name="funcsynopsis.style" select="'ansi'"/>
	<xsl:param name="generate.component.toc" select="1"/>
	<xsl:param name="generate.division.toc" select="1"/>
	<xsl:param name="generate.qandaset.toc" select="1"/>
	<xsl:param name="graphic.default.extension" select="'png'"/>
	<xsl:param name="html.base" select="''"/>
	<xsl:param name="html.stylesheet" select="'look-and-feel.css'"/>
	<xsl:param name="html.stylesheet.type" select="'text/css'"/>
	<xsl:param name="linenumbering.everyNth" select="5"/>
	<xsl:param name="linenumbering.separator" select="' '"/>
	<xsl:param name="linenumbering.width" select="3"/>
	<xsl:param name="link.mailto.url" select="''"/>
	<xsl:param name="nominal.table.width" select="'6in'"/>
	<xsl:param name="part.autolabel" select="1"/>
	<xsl:param name="preface.autolabel" select="1"/>
	<xsl:param name="qanda.defaultlabel" select="1"/>
	<xsl:param name="qanda.inherit.numeration" select="1"/>
	<xsl:param name="qandadiv.autolabel" select="1"/>
	<xsl:param name="refentry.generate.name" select="1"/>
	<xsl:param name="refentry.xref.manvolnum" select="1"/>
	<xsl:param name="rootid" select="''"/>
	<xsl:param name="section.autolabel" select="1"/>
	<xsl:param name="section.label.includes.component.label" select="1"/>
	<xsl:param name="spacing.paras" select="1"/>
	<xsl:param name="stylesheet.result.type" select="'html'"/>
	<xsl:param name="toc.list.type" select="'dl'"/>
	<xsl:param name="toc.section.depth" select="4"/>
	<xsl:param name="ulink.target" select="'_top'"/>
	<xsl:param name="use.id.function" select="1"/>
	<xsl:param name="using.chunker" select="0"/>
</xsl:stylesheet>
