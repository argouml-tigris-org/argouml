<?xml version="1.0" encoding="UTF-8"?>
<!-- $Id$ -->
<!--
// Copyright (c) 1996-2005 The Regents of the University of California. All
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
-->
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" 
	xmlns:saxon="http://icl.com/saxon"
	xmlns:lxslt="http://xml.apache.org/xslt"
	xmlns:xalanredirect="org.apache.xalan.xslt.extensions.Redirect"
	xmlns:doc="http://nwalsh.com/xsl/documentation/1.0"
	exclude-result-prefixes="doc"
	extension-element-prefixes="saxon xalanredirect lxslt">

	<xsl:import href="docbook-xsl/fo/docbook.xsl"/>
	<xsl:import href="commonsettings.xsl"/>
	<xsl:include href="titlepage-pdf.xsl"/>

        <!-- Added by Jeremy Bennett for 1.49 XSL stylesheets -->

        <xsl:variable name="fop.extensions" select="1"/>
        <xsl:variable name="stylesheet.result.type" select="fo"/>
        <xsl:variable name="paper.type" select="A4"/>
        <xsl:variable name="default.table.width" select="'15cm'"/>
        <xsl:variable name="draft.watermark.image"
                      select="docbook-xsl/images/draft.png"/>
                      
  <!-- Added by MVW -->

  <xsl:variable name="generate.toc">
book toc
part nop
chapter nop
  </xsl:variable>

  <!-- variablelist is used with long "variables" consisting
    of methods and their parameters - lay them out stacked instead of
    side-by-side so that they don't overlap -->
  <xsl:variable name="variablelist.as.blocks" select="1"/>

</xsl:stylesheet>
