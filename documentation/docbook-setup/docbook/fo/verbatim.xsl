<?xml version='1.0'?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:sverb="http://nwalsh.com/xslt/ext/com.nwalsh.saxon.Verbatim"
                xmlns:xverb="com.nwalsh.xalan.Verbatim"
                xmlns:lxslt="http://xml.apache.org/xslt"
                exclude-result-prefixes="sverb xverb lxslt"
                version='1.0'>

<!-- ********************************************************************
     $Id$
     ********************************************************************

     This file is part of the XSL DocBook Stylesheet distribution.
     See ../README or http://nwalsh.com/docbook/xsl/ for copyright
     and other information.

     ******************************************************************** -->

<lxslt:component prefix="xverb"
                 functions="numberLines"/>

<xsl:template match="programlisting|screen">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>
  <xsl:variable name="id"><xsl:call-template name="object.id"/></xsl:variable>

  <!-- Obey the <?dbfo linenumbering.everyNth="x"?> PI -->
  <xsl:variable name="pi.linenumbering.everyNth">
    <xsl:call-template name="dbfo-attribute">
      <xsl:with-param name="attribute" select="'everyNth'"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="everyNth">
    <xsl:choose>
      <xsl:when test="$pi.linenumbering.everyNth != ''">
        <xsl:value-of select="$pi.linenumbering.everyNth"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$linenumbering.everyNth"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <!-- Obey the <?dbfo linenumbering.separator="x"?> PI -->
  <xsl:variable name="pi.linenumbering.separator">
    <xsl:call-template name="dbfo-attribute">
      <xsl:with-param name="attribute" select="'linenumbering.separator'"/>
    </xsl:call-template>
  </xsl:variable>

  <xsl:variable name="separator">
    <xsl:choose>
      <xsl:when test="$pi.linenumbering.separator != ''">
        <xsl:value-of select="$pi.linenumbering.separator"/>
      </xsl:when>
      <xsl:otherwise>
        <xsl:value-of select="$linenumbering.separator"/>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $use.extensions != '0'
                    and $linenumbering.extension != '0'">
      <xsl:variable name="rtf">
        <xsl:apply-templates/>
      </xsl:variable>
      <fo:block wrap-option='no-wrap'
                text-align='start'
                white-space-collapse='false'
                linefeed-treatment="preserve"
                font-family='{$monospace.font.family}'
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:call-template name="number.rtf.lines">
          <xsl:with-param name="rtf" select="$rtf"/>
          <xsl:with-param name="linenumbering.everyNth"
                          select="$everyNth"/>
          <xsl:with-param name="linenumbering.width"
                          select="$linenumbering.width"/>
          <xsl:with-param name="linenumbering.separator"
                          select="$separator"/>
        </xsl:call-template>
      </fo:block>
    </xsl:when>
    <xsl:otherwise>
      <fo:block wrap-option='no-wrap'
                text-align='start'
                white-space-collapse='false'
                linefeed-treatment="preserve"
                font-family='{$monospace.font.family}'
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:apply-templates/>
      </fo:block>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="literallayout">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>

  <xsl:variable name="rtf">
    <xsl:apply-templates/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $use.extensions != '0'
                    and $linenumbering.extension != '0'">
      <xsl:choose>
        <xsl:when test="@class='monospaced'">
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    linefeed-treatment="preserve"
                    font-family='{$monospace.font.family}'
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:call-template name="number.rtf.lines">
              <xsl:with-param name="rtf" select="$rtf"/>
              <xsl:with-param name="linenumbering.everyNth"
                              select="$linenumbering.everyNth"/>
              <xsl:with-param name="linenumbering.width"
                              select="$linenumbering.width"/>
              <xsl:with-param name="linenumbering.separator"
                              select="$linenumbering.separator"/>
            </xsl:call-template>
          </fo:block>
        </xsl:when>
        <xsl:otherwise>
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    linefeed-treatment="preserve"
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:call-template name="number.rtf.lines">
              <xsl:with-param name="rtf" select="$rtf"/>
              <xsl:with-param name="linenumbering.everyNth"
                              select="$linenumbering.everyNth"/>
              <xsl:with-param name="linenumbering.width"
                              select="$linenumbering.width"/>
              <xsl:with-param name="linenumbering.separator"
                              select="$linenumbering.separator"/>
            </xsl:call-template>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:when>
    <xsl:otherwise>
      <xsl:choose>
        <xsl:when test="@class='monospaced'">
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    font-family='{$monospace.font.family}'
                    linefeed-treatment="preserve"
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:copy-of select="$rtf"/>
          </fo:block>
        </xsl:when>
        <xsl:otherwise>
          <fo:block wrap-option='no-wrap'
                    text-align='start'
                    linefeed-treatment="preserve"
                    white-space-collapse='false'
                    space-before.minimum="0.8em"
                    space-before.optimum="1em"
                    space-before.maximum="1.2em">
            <xsl:copy-of select="$rtf"/>
          </fo:block>
        </xsl:otherwise>
      </xsl:choose>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template match="address">
  <xsl:param name="suppress-numbers" select="'0'"/>
  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>

  <xsl:variable name="rtf">
    <xsl:apply-templates/>
  </xsl:variable>

  <xsl:choose>
    <xsl:when test="$suppress-numbers = '0'
                    and @linenumbering = 'numbered'
                    and $use.extensions != '0'
                    and $linenumbering.extension != '0'">
      <fo:block wrap-option='no-wrap'
                white-space-collapse='false'
                linefeed-treatment="preserve"
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:call-template name="number.rtf.lines">
          <xsl:with-param name="rtf" select="$rtf"/>
          <xsl:with-param name="linenumbering.everyNth"
                          select="$linenumbering.everyNth"/>
          <xsl:with-param name="linenumbering.width"
                          select="$linenumbering.width"/>
          <xsl:with-param name="linenumbering.separator"
                          select="$linenumbering.separator"/>
        </xsl:call-template>
      </fo:block>
    </xsl:when>
    <xsl:otherwise>
      <fo:block wrap-option='no-wrap'
                text-align='start'
                linefeed-treatment="preserve"
                white-space-collapse='false'
                space-before.minimum="0.8em"
                space-before.optimum="1em"
                space-before.maximum="1.2em">
        <xsl:apply-templates/>
      </fo:block>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

<xsl:template name="number.rtf.lines">
  <xsl:param name="rtf" select="''"/>
  <!-- the following parameters must have these names ... -->
  <xsl:param name="linenumbering.everyNth" select="1"/>
  <xsl:param name="linenumbering.width" select="3"/>
  <xsl:param name="linenumbering.separator" select="' |'"/>

  <xsl:variable name="vendor" select="system-property('xsl:vendor')"/>

  <xsl:choose>
    <xsl:when test="contains($vendor, 'SAXON ')">
      <xsl:copy-of select="sverb:numberLines($rtf)"/>
    </xsl:when>
    <xsl:when test="contains($vendor, 'Apache Software Foundation')">
      <xsl:copy-of select="xverb:numberLines($rtf)"/>
    </xsl:when>
    <xsl:otherwise>
      <xsl:message terminate="yes">
        <xsl:text>Don't know how to do line numbering with </xsl:text>
        <xsl:value-of select="$vendor"/>
      </xsl:message>
    </xsl:otherwise>
  </xsl:choose>
</xsl:template>

</xsl:stylesheet>

