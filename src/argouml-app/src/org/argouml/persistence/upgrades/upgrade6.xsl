<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:preserve-space elements="uml"/>


<!-- Make sure pgml members appear immediately after xmi -->
    
    <!-- Write <member> tags within <members> ordered by profile, xmi, pgml, todo -->
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
	
	<!-- Remove any groups containing LAYER_NULL issue 5247 -->
 	<xsl:template match="/uml/pgml/group[contains(./private,'LAYER_NULL')]">
	</xsl:template>
	
	<!-- Add defaults for new settings issue 5231 and 535-->
 	<xsl:template match="/uml/argo/settings">
 	   <settings>
		<xsl:copy-of select="*"/>
		<xsl:if test="count(showboldnames) = 0">
			<showboldnames>false</showboldnames>
		</xsl:if>
		<xsl:if test="count(showassociationnames) = 0">
			<showassociationnames>true</showassociationnames>
		</xsl:if>
		<xsl:if test="count(fontname) = 0">
			<fontname>Dialog</fontname>
		</xsl:if>
		<xsl:if test="count(fontsize) = 0">
			<fontsize>10</fontsize>
		</xsl:if>
		<xsl:if test="count(defaultstereotypeview) = 0">
			<defaultstereotypeview>0</defaultstereotypeview>
		</xsl:if>
		<xsl:if test="count(hidebidirectionalarrows) = 0">
			<hidebidirectionalarrows>true</hidebidirectionalarrows>
		</xsl:if>
 	   </settings>
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
