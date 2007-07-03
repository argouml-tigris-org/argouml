<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
	<xsl:output method="xml" indent="yes"/>
	<xsl:preserve-space elements="uml"/>


<!-- Remove collaboration diagrams containing no items with no namespace (issue 3311) -->
	<xsl:template match='pgml[@description = "org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram" and count(./group) = 0]' />
	


<!-- Make sure pgml members appear immediately after xmi -->
    <!-- First remove all pgml members from wherever they are-->
	<xsl:template match='member[./@type = "pgml"]'>
    </xsl:template>
    
    <!-- Now reintroduce pgml members after xmi member -->
	<xsl:template match='member[./@type = "xmi"]'>
        <member type="{@type}" name="{@name}"/>
        <xsl:for-each select='/uml/pgml[@description != "org.argouml.uml.diagram.collaboration.ui.UMLCollaborationDiagram" or count(./group) != 0]'>
            <member type="pgml" name="diagram"/>
        </xsl:for-each>
    </xsl:template>



<!-- 
Fix Association Classes so that the dashed edge has both a source and
destination node. This involves creating a new node Fig (FigAssociationClassTee)
at the junction of the association edge and the dashed edge.
-->
	
	<!-- Create a FigAssociationClassTee before any FigClassAssociationClass -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.ui.FigClassAssociationClass")]'>
		
		<xsl:variable name="fig-id" select="@name"/>
		<xsl:variable name="href" select="@href"/>

		<xsl:variable name="x" select='../group[./@description = "org.argouml.uml.diagram.ui.FigEdgeAssociationClass" and ./@href = $href]/path/moveto/@x'/>
		<xsl:variable name="y" select='../group[./@description = "org.argouml.uml.diagram.ui.FigEdgeAssociationClass" and ./@href = $href]/path/moveto/@y'/>
		
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
        <group name="tee_{$fig-id}"
             description="org.argouml.uml.diagram.ui.FigAssociationClassTee[{$x - 9}, {$y - 3}, 10, 10]"
             href="{@href}"
             fill="{@fill}"
             fillcolor="{@fillcolor}"
             stroke="{@stroke}"
             strokecolor="{@strokecolor}"
        >
          <private>
          </private>
    
          <ellipse name="tee_{$fig-id}.0"
            x="{$x}"
            y="{$y}"
            rx="5"
            ry="5"
            fill="{@fill}"
            fillcolor="{@fillcolor}"
            stroke="{@stroke}"
            strokecolor="{@strokecolor}"
          />
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
          <xsl:value-of select="$private" />sourcePortFig="tee_<xsl:value-of select="$destFigNode" />
          sourceFigNode="tee_<xsl:value-of select="$destFigNode" />
      </private>
	
			<xsl:apply-templates/>
	</xsl:template>
	
	<!-- Specifically ignore these nodes as they are handled above-->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.ui.FigEdgeAssociationClass")]/private/text()' />


<!-- 
There are corrupt entries in the todo files of some users saves.. The cause of
these is not yet known. One effect is that there are spaces embedded within the
class names of posters and offenders.
Some unusual style of encoding has been used inside todo files which means that
a space is saved as %32;. Fix 1 and Fix 2 below will remove any items that
multiple spaces in the class name.
A second issue is related to the fact that we currently have a Singleton list of
ToDo items foe the application. We should really have a list of todo items per
project.
The current situation means that if errors occur in other parts of ArgoUML it 
may be possible for todo items to be carried from one project to another.
Fix 3 attempts to resolve this by removing any todo item that refer to
non-existent model elements.

See issue 3134 and issue 3297.
-->

	
	<!-- ToDoItems Fix 1 Removes any resolved items containing a corrupt poster -->
	<xsl:template match='/uml/todo/resolvedcritics/issue[contains(poster/text(), "%32;%32;")]'/>
	
	<!-- ToDoItems Fix 2 Removes any resolved items containing a corrupt offender -->
	<xsl:template match='/uml/todo/resolvedcritics/issue[contains(offender/text(), "%32;%32;")]'/>

	<!-- ToDoItems Fix 3 Only copy over resolved issues that refer to an offender in the XMI
	     Perfomance improvment 1. It may be possible to remove the if clause and combine
	     it with the template match to speed this up.
	     Performance improvement 2. "count" is probably not the most efficient way to determine
	     if any instance exists, investigate a better way.
	-->
	<!-- key definition for UUIDs -->
        <xsl:key name="uuid-key" match="*" use="@xmi.uuid"/>
	<!-- Now for each resolved critic only copy it across if its uuid is
	     in that list -->
	<xsl:template match='/uml/todo/resolvedcritics/issue'>
		<xsl:variable name="offender" select="offender/text()" />
        <xsl:variable name="uuid" select="key('uuid-key',string(.))/@xmi.uuid"/>
		<xsl:if test="$uuid">
			<issue>
				<poster><xsl:value-of select="poster/text()" /></poster>
				<offender><xsl:value-of select="$offender" /></offender>
			</issue>
		</xsl:if>
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
