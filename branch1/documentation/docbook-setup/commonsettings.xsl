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
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
  <xsl:variable name="admon.graphics" select="1"/>
  <xsl:variable name="admon.graphics.path" select="'images/'"/>
  <xsl:variable name="admon.style"
                select="'margin-left: 0.5in; margin-right: 0.5in;'"/>
  <xsl:variable name="author.othername.in.middle" select="1"/>
  <xsl:variable name="biblioentry.item.separator" select="'. '"/>
  <xsl:variable name="callout.defaultcolumn" select="60"/>
  <xsl:variable name="callout.graphics" select="1"/>
  <xsl:variable name="callout.graphics.extension" select="'.png'"/>
  <xsl:variable name="callout.graphics.number.limit" select="10"/>
  <xsl:variable name="callout.graphics.path" select="'images/callouts/'"/>
  <xsl:variable name="callout.list.table" select="1"/>
  <xsl:variable name="chapter.autolabel" select="1"/>
  <xsl:variable name="check.idref" select="0"/>
  <xsl:variable name="css.decoration" select="1"/>
  <xsl:variable name="default.table.width" select="''"/>
  <xsl:variable name="funcsynopsis.decoration" select="1"/>
  <xsl:variable name="funcsynopsis.style" select="'ansi'"/>
  <xsl:variable name="generate.component.toc" select="1"/>
  <xsl:variable name="generate.division.toc" select="1"/>
  <xsl:variable name="generate.qandaset.toc" select="1"/>
  <xsl:variable name="graphic.default.extension" select="'png'"/>
  <xsl:variable name="html.base" select="''"/>
  <xsl:variable name="html.stylesheet" select="'look-and-feel.css'"/>
  <xsl:variable name="html.stylesheet.type" select="'text/css'"/>
  <xsl:variable name="linenumbering.everyNth" select="5"/>
  <xsl:variable name="linenumbering.separator" select="' '"/>
  <xsl:variable name="linenumbering.width" select="3"/>
  <xsl:variable name="link.mailto.url" select="''"/>
  <xsl:variable name="nominal.table.width" select="'6in'"/>
  <xsl:variable name="part.autolabel" select="1"/>
  <xsl:variable name="preface.autolabel" select="1"/>
  <xsl:variable name="qanda.defaultlabel" select="1"/>
  <xsl:variable name="qanda.inherit.numeration" select="1"/>
  <xsl:variable name="qandadiv.autolabel" select="1"/>
  <xsl:variable name="refentry.generate.name" select="1"/>
  <xsl:variable name="refentry.xref.manvolnum" select="1"/>
  <xsl:variable name="rootid" select="''"/>
  <xsl:variable name="section.autolabel" select="1"/>
  <xsl:variable name="section.label.includes.component.label" select="1"/>
  <xsl:variable name="spacing.paras" select="1"/>
  <xsl:variable name="stylesheet.result.type" select="'html'"/>
  <xsl:variable name="toc.list.type" select="'dl'"/>
  <xsl:variable name="toc.section.depth" select="2"/>
  <xsl:variable name="ulink.target" select="'_top'"/>
  <xsl:variable name="use.id.function" select="1"/>
  <xsl:variable name="using.chunker" select="0"/>

  <!-- Added by Jeremy Bennett for 1.49 XSL stylesheets -->

  <xsl:variable name="saxon.extensions" select="1"/>

</xsl:stylesheet>

