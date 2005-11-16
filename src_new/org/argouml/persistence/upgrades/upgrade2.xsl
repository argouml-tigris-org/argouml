<?xml version="1.0"?>
<!-- -->
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:preserve-space elements="uml"/>

<!-- remove the contents old style sequence diagrams -->
    <!-- First remove all pgml members -->
	<xsl:template match='member[./@type = "pgml"]'>
    </xsl:template>
    
    <!-- Then remove the sequence diagrams -->
	<xsl:template match='pgml[starts-with(./@description, "org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram")]'>
    </xsl:template>
    
    <!-- Now reintroduce pgml members based on new number of diagrams -->
	<xsl:template match='member[./@type = "xmi"]'>
        <member type="{@type}" name="{@name}"/>
        <xsl:for-each select="/uml/pgml[starts-with(./@description, 'org.argouml.uml.diagram.sequence.ui.UMLSequenceDiagram')=false]">
            <member type="pgml" name="diagram"/>
        </xsl:for-each>
    </xsl:template>


    
	<!-- convert the first group under a FigClass to a FigAttributesCompartment -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigClass[")]/group[1]'>
		
		<xsl:call-template name="compartment">
			<xsl:with-param name="classifier-descr" select="../@description"/>
			<xsl:with-param name="compartment-descr" select="@description"/>
			<xsl:with-param name="new-class" select="'org.argouml.uml.diagram.ui.FigAttributesCompartment'"/>
			<xsl:with-param name="name" select="@name" />
			<xsl:with-param name="fill" select="@fill" />
			<xsl:with-param name="fillcolor" select="@fillcolor" />
			<xsl:with-param name="stroke" select="@stroke" />
			<xsl:with-param name="strokecolor" select="@strokecolor" />
		</xsl:call-template>
			<xsl:apply-templates/>
	</xsl:template>
	
	
	<!-- convert the second group under a FigClass to a FigOperationsCompartment -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigClass[")]/group[2]'>
		
		<xsl:call-template name="compartment">
			<xsl:with-param name="classifier-descr" select="../@description"/>
			<xsl:with-param name="compartment-descr" select="@description"/>
			<xsl:with-param name="new-class" select="'org.argouml.uml.diagram.ui.FigOperationsCompartment'"/>
			<xsl:with-param name="name" select="@name" />
			<xsl:with-param name="fill" select="@fill" />
			<xsl:with-param name="fillcolor" select="@fillcolor" />
			<xsl:with-param name="stroke" select="@stroke" />
			<xsl:with-param name="strokecolor" select="@strokecolor" />
		</xsl:call-template>
			<xsl:apply-templates/>
	</xsl:template>


	
	<!-- convert the first (and presumably only) group under a FigInterface to a FigOperationsCompartment -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigInterface[")]/group[1]'>
		
		<xsl:call-template name="compartment">
			<xsl:with-param name="classifier-descr" select="../@description"/>
			<xsl:with-param name="compartment-descr" select="@description"/>
			<xsl:with-param name="new-class" select="'org.argouml.uml.diagram.ui.FigOperationsCompartment'"/>
			<xsl:with-param name="name" select="@name" />
			<xsl:with-param name="fill" select="@fill" />
			<xsl:with-param name="fillcolor" select="@fillcolor" />
			<xsl:with-param name="stroke" select="@stroke" />
			<xsl:with-param name="strokecolor" select="@strokecolor" />
		</xsl:call-template>
			<xsl:apply-templates/>
	</xsl:template>
	

	<!-- Specifically ignore these nodes -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigClass[")]/group/private' />
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigInterface[")]/group/private'/>
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigClass[")]/group/rectangle'/>
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigInterface[")]/group/rectangle'/>
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigClass[")]/group/text'/>
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigInterface[")]/group/text'/>
	
	
	<!-- copy all other nodes over unchanged -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>


	<!-- method to build a new compartment -->
	<xsl:template name="compartment">
		<xsl:param name="classifier-descr" />
		<xsl:param name="compartment-descr" />
		<xsl:param name="new-class" />
		<xsl:param name="name" />
		<xsl:param name="fill" />
		<xsl:param name="fillcolor" />
		<xsl:param name="stroke" />
		<xsl:param name="strokecolor" />
		<xsl:variable name="classifier-bounds" select="substring-after($classifier-descr,'[')"/>
		<xsl:variable name="compartment-bounds" select="substring-after($compartment-descr,'[')"/>
		
		<xsl:variable name="compartment-y" select="normalize-space(substring-before(substring-after($compartment-bounds, ','), ','))" />
		<xsl:variable name="compartment-height" select="normalize-space(substring-before(substring-after(substring-after(substring-after($compartment-bounds, ','), ','), ','), ']'))" />
		<xsl:variable name="classifier-y" select="normalize-space(substring-before(substring-after($classifier-bounds, ','), ','))" />
		<xsl:variable name="classifier-height" select="normalize-space(substring-before(substring-after(substring-after(substring-after($classifier-bounds, ','), ','), ','), ']'))" />

		<xsl:choose>
			<xsl:when test="$compartment-height = '0'">
				<group name="{$name}"
				       description="{$new-class}[]"
				       fill="{$fill}"
				       fillcolor="{$fillcolor}"
				       stroke="{$stroke}"
				       strokecolor="{$strokecolor}">
					<xsl:copy-of select="./node()"/>
				</group>
			</xsl:when>
			<xsl:when test="$compartment-y &gt;= ($classifier-y + $classifier-height)">
				<group name="{$name}"
				       description="{$new-class}[]"
				       fill="{$fill}"
				       fillcolor="{$fillcolor}"
				       stroke="{$stroke}"
				       strokecolor="{$strokecolor}">
					<xsl:copy-of select="./node()"/>
				</group>
			</xsl:when>
			<xsl:otherwise>
				<group name="{$name}"
				       description="{$new-class}[{$compartment-bounds}"
				       fill="{$fill}"
				       fillcolor="{$fillcolor}"
				       stroke="{$stroke}"
				       strokecolor="{$strokecolor}">
					<xsl:copy-of select="./node()"/>
				</group>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<!-- method to build a new compartment -->
	<xsl:template name="get-height">
		<xsl:param name="bounds" />
		
		<xsl:variable name="compartment-height" select="normalize-space(substring-before(substring-after(substring-after(substring-after($bounds, ','), ','), ','), ']'))" />
	</xsl:template>
</xsl:stylesheet>
