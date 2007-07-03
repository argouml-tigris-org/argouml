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
	
    <xsl:template match='Behavioral_Elements.Activity_Graphs.ActivityGraph/Behavioral_Elements.State_Machines.StateMachine.top/Behavioral_Elements.State_Machines.CompositeState/Behavioral_Elements.State_Machines.CompositeState.subvertex/Behavioral_Elements.Activity_Graphs.ActionState[count(Behavioral_Elements.State_Machines.State.entry) = 0]'>
      <Behavioral_Elements.Activity_Graphs.ActionState xmi.id="{@xmi.id}" xmi.uuid="{@xmi.uuid}">
      <Foundation.Core.ModelElement.name></Foundation.Core.ModelElement.name>
      <xsl:copy-of select='./*[name() != "Foundation.Core.ModelElement.name"]'/>
		<Behavioral_Elements.State_Machines.State.entry>
		  <Behavioral_Elements.Common_Behavior.UninterpretedAction xmi.id="update3.1_id1_{generate-id()}" xmi.uuid="update3.1_id2_{generate-id()}">
			<Foundation.Core.ModelElement.isSpecification xmi.value="false"/>
			<Behavioral_Elements.Common_Behavior.Action.isAsynchronous xmi.value="false"/>
			<Behavioral_Elements.Common_Behavior.Action.script>
			    <Foundation.Data_Types.ActionExpression xmi.id="update3.1_id3_{generate-id()}">
					<Foundation.Data_Types.Expression.language />
					<Foundation.Data_Types.Expression.body><xsl:value-of select="Foundation.Core.ModelElement.name" /></Foundation.Data_Types.Expression.body>
				</Foundation.Data_Types.ActionExpression>
			</Behavioral_Elements.Common_Behavior.Action.script>
		  </Behavioral_Elements.Common_Behavior.UninterpretedAction>
		</Behavioral_Elements.State_Machines.State.entry>
    </Behavioral_Elements.Activity_Graphs.ActionState>
    			<xsl:apply-templates/>                
	</xsl:template>
	
	
	<!-- Specifically ignore these nodes as they are handled above-->
	<xsl:template match='Behavioral_Elements.Activity_Graphs.ActivityGraph/Behavioral_Elements.State_Machines.StateMachine.top/Behavioral_Elements.State_Machines.CompositeState/Behavioral_Elements.State_Machines.CompositeState.subvertex/Behavioral_Elements.Activity_Graphs.ActionState/*' />


<!-- 
For for http://argouml.tigris.org/issues/show_bug.cgi?id=716
Change pseudostate kind from branch to junction (unless we're 0.17.??
-->
	<xsl:template match='Behavioral_Elements.State_Machines.Pseudostate.kind[./@xmi.value = "branch"]'>
		<xsl:choose>
			<xsl:when test="contains(/uml/argo/documentation/version, '0.17.')">
				<Behavioral_Elements.State_Machines.Pseudostate.kind xmi.value="branch"/>
			</xsl:when>
			<xsl:otherwise>
				<Behavioral_Elements.State_Machines.Pseudostate.kind xmi.value="junction"/>
			</xsl:otherwise>
		</xsl:choose>
    			<xsl:apply-templates/>                
	</xsl:template>
	
	<!-- Convert any FigBranchState to FigJunctionState -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.state.ui.FigBranchState[") and count(./path) != 0]'>
		<xsl:variable name="bounds" select='substring-after(./@description, "org.argouml.uml.diagram.state.ui.FigBranchState")'/>
		<group name="{@name}"
			description="org.argouml.uml.diagram.state.ui.FigJunctionState{$bounds}"
			href="{@href}"
			fill="{@fill}"
			fillcolor="{@fillcolor}"
			stroke="{@color}"
			strokecolor="{@strokecolor}"
		>
			<xsl:copy-of select="./node()"/>
		</group>
			<xsl:apply-templates/>
	</xsl:template>
	
	<!-- Specifically ignore these nodes -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.state.ui.FigBranchState[") and count(./path) != 0]/*' />


	<!--
    Fix issue http://argouml.tigris.org/issues/show_bug.cgi?id=2845
    Insert a statemachine into any action state without one
    -->
	
	<xsl:template match='Behavioral_Elements.State_Machines.CompositeState.isConcurent'>
	  <Behavioral_Elements.State_Machines.CompositeState.isConcurrent xmi.value="{@xmi.value}"/>
    			<xsl:apply-templates/>                
	</xsl:template>
	



    <!-- 
    Fix issue http://argouml.tigris.org/issues/show_bug.cgi?id=2997
	Determine which compartments are invible and set a marker in the classifier description
    -->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigClass[")]'>
		<xsl:choose>
			<xsl:when test="group/@description = 'org.argouml.uml.diagram.ui.FigOperationsCompartment[]' and group/@description = 'org.argouml.uml.diagram.ui.FigAttributesCompartment[]'">
				<group name="{@name}"
					 description="{@description}attributesVisible=false;operationsVisible=false"
					 href="{@href}"
					 fill="{@fill}"
					 fillcolor="{@fillcolor}"
					 stroke="{@stroke}"
					 strokecolor="{@strokecolor}"
				>
          <xsl:copy-of select="./node()"/>
        </group>
			</xsl:when>
			<xsl:when test="group/@description = 'org.argouml.uml.diagram.ui.FigOperationsCompartment[]'">
				<group name="{@name}"
					 description="{@description}operationsVisible=false"
					 href="{@href}"
					 fill="{@fill}"
					 fillcolor="{@fillcolor}"
					 stroke="{@stroke}"
					 strokecolor="{@strokecolor}"
				>
          <xsl:copy-of select="./node()"/>
        </group>
			</xsl:when>
			<xsl:when test="group/@description = 'org.argouml.uml.diagram.ui.FigAttributesCompartment[]'">
				<group name="{@name}"
					 description="{@description}attributesVisible=false"
					 href="{@href}"
					 fill="{@fill}"
					 fillcolor="{@fillcolor}"
					 stroke="{@stroke}"
					 strokecolor="{@strokecolor}"
				>
          <xsl:copy-of select="./node()"/>
        </group>
			</xsl:when>
			<xsl:otherwise>
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
			</xsl:otherwise>
		</xsl:choose>
    			<xsl:apply-templates/>                
	</xsl:template>
	
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigInterface[")]'>
		<xsl:choose>
			<xsl:when test="group/@description = 'org.argouml.uml.diagram.ui.FigOperationsCompartment[]'">
				<group name="{@name}"
					 description="{@description}operationsVisible=false"
					 href="{@href}"
					 fill="{@fill}"
					 fillcolor="{@fillcolor}"
					 stroke="{@stroke}"
					 strokecolor="{@strokecolor}"
				>
                    <xsl:copy-of select="./node()"/>
                </group>
			</xsl:when>
			<xsl:otherwise>
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
			</xsl:otherwise>
		</xsl:choose>
    			<xsl:apply-templates/>                
	</xsl:template>


	<!-- Specifically ignore these nodes as they are handled above-->
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigClass[")]/*' />
	<xsl:template match='group[starts-with(./@description, "org.argouml.uml.diagram.static_structure.ui.FigInterface[")]/*' />

	<!-- copy all other nodes over unchanged -->
	<xsl:template match="@*|node()">
		<xsl:copy>
			<xsl:apply-templates select="@*|node()"/>
		</xsl:copy>
	</xsl:template>
</xsl:stylesheet>
