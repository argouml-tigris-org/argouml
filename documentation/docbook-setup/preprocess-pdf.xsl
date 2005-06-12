<?xml version='1.0'?>

<!-- ===================================================
Solves a problem with the docbook-xsl stylesheets
and FOP 0.20.5.

This stylesheet wraps a <phrase> element around the
contents of a variablist/term. If this is not done, FOP
runs into an endless loop when it encounters a <guiicon>
in a variablist/term.
Reason: the docbook-xsl stylesheets obviously make a wrong 
computation of the width of the variablelist term in this case, 
and FOP can't handle this.
======================================================== --> 

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
		xmlns:fo="http://www.w3.org/1999/XSL/Format"
                xmlns:db="http://docbook.org"
                version='1.0'>


<xsl:output method="xml" indent="yes"/>

<xsl:template match="@*|node()|processing-instruction()">
  <xsl:copy>
    <xsl:apply-templates select="@*|node()|processing-instruction()"/>
  </xsl:copy>
</xsl:template>

<xsl:template match="@float">
</xsl:template>

<xsl:template match="varlistentry/term">
  <xsl:copy>
    <xsl:element name="phrase">
      <xsl:apply-templates select="@*|node()"/>
    </xsl:element>
  </xsl:copy>
</xsl:template>
 
</xsl:stylesheet>