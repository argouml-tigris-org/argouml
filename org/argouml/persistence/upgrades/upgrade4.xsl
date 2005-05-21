<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:preserve-space elements="uml"/>

<!-- 
Fix for http://argouml.tigris.org/issues/show_bug.cgi?id=713

Searches for any nodes with the path
    Behavioral_Elements.Activity_Graphs.ActivityGraph/
      Behavioral_Elements.State_Machines.StateMachine.top/
      Behavioral_Elements.State_Machines.CompositeState/
      Behavioral_Elements.State_Machines.CompositeState.subvertex/
      Behavioral_Elements.Activity_Graphs.ActionState

that does not have any child node of Behavioral_Elements.State_Machines.State.entry

For each found
- the child node will be created.
- the model element name will be moved to  Foundation.Data_Types.Expression.body
-->
	
	<!-- Convert a FigAssociationClassTee before any FigClassAssociationClass -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.ui.FigClassAssociationClass")]'>
		
		<xsl:variable name="fig-id" select="@name"/>
		
        <group name="tee.{$fig-id}"
             description="org.argouml.uml.diagram.ui.FigAssociationClassTee[185, 51, 10, 10]"
             href="{@href}"
             fill="{@fill}"
             fillcolor="{@fillcolor}"
             stroke="{@stroke}"
             strokecolor="{@strokecolor}"
        >
          <private>
          </private>
    
          <ellipse name="tee.{$fig-id}.0"
            x="190"
            y="56"
            rx="5"
            ry="5"
            fill="{@fill}"
            fillcolor="{@fillcolor}"
            stroke="{@stroke}"
            strokecolor="{@strokecolor}"
          />
        </group>		
		<group name="{@name}"
			description="{@description}"
			href="{@href}"
			fill="{@fill}"
			fillcolor="{@fillcolor}"
			stroke="{@stroke}"
			strokecolor="{@strokecolor}"
		>
			<xsl:copy-of select="./node()"/>
		</group>
		
			<xsl:apply-templates/>
	</xsl:template>
	
	<!-- Specifically ignore these nodes as they are handled above-->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.ui.FigClassAssociationClass")]/*' />
	

	<!-- Add a sourceFigNode to the FigEdgeAssociationClass -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.ui.FigEdgeAssociationClass")]/private'>
		
		<xsl:variable name="private" select="text()"/>
		<xsl:variable name="destFigNode" select="substring(substring-after($private,'destFigNode='),2)"/>
      <private>
          <xsl:value-of select="$private" />sourcePortFig="tee.<xsl:value-of select="$destFigNode" />
          sourceFigNode="tee.<xsl:value-of select="$destFigNode" />
      </private>
	
			<xsl:apply-templates/>
	</xsl:template>
	
	<!-- Specifically ignore these nodes as they are handled above-->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.ui.FigEdgeAssociationClass")]/private/text()' />
	
	<!-- Removes any resolved items containing a corruption of multiple spaces -->
	<xsl:template match='/uml/todo/resolvedcritics/issue[contains(poster/text(), "%32;%32;")]' />
	<xsl:template match='/uml/todo/resolvedcritics/issue[contains(offender/text(), "%32;%32;")]' />
	<!--xsl:template match='/uml/todo/resolvedcritics/issue[count( /uml/XMI/XMI.content//@xmi.uuid = "sdf") = 0 ]' /-->

	<!-- Onlt copy over resolved issues that refer to an offender in the XMI -->
	<xsl:template match='/uml/todo/resolvedcritics/issue'>
		<xsl:variable name="offender" select="offender/text()" />
		<xsl:if test="count(/uml/XMI/XMI.content//*[@xmi.uuid = $offender]) != 0">
			<issue>
				<poster><xsl:value-of select="poster/text()" /></poster>
				<offender><xsl:value-of select="$offender" /></offender>
			</issue>
		</xsl:if>
	</xsl:template>

	<!-- copy all other nodes over unchanged -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
