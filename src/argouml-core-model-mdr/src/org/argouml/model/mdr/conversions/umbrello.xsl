<?xml version="1.0" encoding="UTF-8"?>
<!-- 
  Add licence here...
  
  Author: Laurent Braud
  Purpose: Issue 5816 - Import XMI from Umbrello
  
  The first package (Logical View,...) has an invalid namespace
  
  
-->
<xsl:stylesheet version="2.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
     <xsl:output method="xml" indent="yes" encoding="UTF-8" />

    <xsl:template match="*">
       <!-- <xsl:copy-of select="."/> -->
	   <xsl:copy> <xsl:apply-templates select="@* | node()"/> </xsl:copy>

    </xsl:template>
    
    <xsl:template match="@*">
		<xsl:choose>
			<xsl:when test="name()='namespace' and contains(.,' ')">
				<xsl:attribute name="namespace"><xsl:value-of select="translate(.,' ', '_')"/></xsl:attribute>
			</xsl:when>
			<xsl:otherwise>
				<xsl:copy> <xsl:apply-templates select="@*"/> </xsl:copy>
			</xsl:otherwise>
		</xsl:choose>
		
    </xsl:template>
</xsl:stylesheet>