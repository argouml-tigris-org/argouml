<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:preserve-space elements="uml"/>


<!-- Make sure pgml members appear immediately after xmi -->
    
    <!-- Write member tags within members as profile, xmi, pgml, todo -->
	<xsl:template match='member[1]'>
		<members>
	        <xsl:for-each select='/uml/argo/member[./@type = "profile"]'>
	        		<member>
					<xsl:copy-of select="@*"/>
	        		</member>
	        </xsl:for-each>
	        <xsl:for-each select='/uml/argo/member[./@type = "xmi"]'>
	        		<member>
					<xsl:copy-of select="@*"/>
	        		</member>
	        </xsl:for-each>
	        <xsl:for-each select='/uml/argo/member[./@type = "pgml"]'>
	        		<member>
					<xsl:copy-of select="@*"/>
	        		</member>
	        </xsl:for-each>
	        <xsl:for-each select='/uml/argo/member[./@type = "todo"]'>
	        		<member>
					<xsl:copy-of select="@*"/>
	        		</member>
	        </xsl:for-each>
	        <!-- Any other members that may be defined by plugins -->
	        <xsl:for-each select='/uml/argo/member[./@type = "profile" and ./@type = "xmi" and ./@type = "pgml" and ./@type = "todo"]'>
	        		<member>
					<xsl:copy-of select="@*"/>
	        		</member>
	        </xsl:for-each>
		</members>
    </xsl:template>

	<xsl:template match='member[position() > 1]'>
	</xsl:template>
	
	<!-- Remove any left over references to a tee_ prefixed Fig id issue 5075 -->
 	<xsl:template match="/uml/pgml/group[./@description='org.argouml.uml.diagram.ui.FigEdgeAssociationClass']/private[contains(.,'sourcePortFig=&quot;tee_')]">
	   <private>
		<xsl:value-of select="substring-before(.,'sourcePortFig=&quot;tee_')" />
	   </private>
	</xsl:template>
    
<!-- 
Anything not touched by the fixes above must be copied over unchanged
-->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
