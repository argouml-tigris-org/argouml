<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="XMI">
		<menu.XMI>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</menu.XMI>
	</xsl:template>

	<xsl:template match="XMI.content">
		<menu.XMIContent>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="Model_Management.Model/Foundation.Core.ModelElement.name"/>
			<xsl:apply-templates select="descendant::Model_Management.Model"/>
		</menu.XMIContent>
	</xsl:template>
		
	<!-- Model Name -->
	<xsl:template match="Model_Management.Model">
		<!--<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))"> -->
				<menu.ModelManagement>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates select="descendant::Model_Management.Package"/>
					<xsl:apply-templates select="descendant::Foundation.Core.Class"/> 
					<xsl:apply-templates select="descendant::Foundation.Core.Interface"/> 
					<xsl:apply-templates select="descendant::Foundation.Core.Association"/>
					<xsl:apply-templates select="descendant::Foundation.Core.Abstraction"/>
					<xsl:apply-templates select="descendant::Foundation.Core.Generalization"/>
					<xsl:apply-templates select="descendant::Behavioral_Elements.Use_Cases.Actor"/>
					<xsl:apply-templates select="descendant::Behavioral_Elements.Use_Cases.UseCase"/>
					<xsl:apply-templates select="descendant::Behavioral_Elements.Common_Behavior.Object"/>
					<xsl:apply-templates select="descendant::Behavioral_Elements.Common_Behavior.Link"/>
					<xsl:apply-templates select="descendant::Behavioral_Elements.Common_Behavior.Stimulus"/>
					<xsl:apply-templates select="descendant::Behavioral_Elements.Common_Behavior.CallAction"/>
					<xsl:apply-templates select="descendant::Behavioral_Elements.Activity_Graphs.ActivityGraph"/>
				</menu.ModelManagement>
			<!--</xsl:if>
		</xsl:if>-->
	</xsl:template>

	<!-- package -->
	<xsl:template match="Model_Management.Package">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="package">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- class -->
	<xsl:template match="Foundation.Core.Class">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="class">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates select="descendant::Foundation.Core.Attribute|descendant::Foundation.Core.Operation"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- interface -->
	<xsl:template match="Foundation.Core.Interface">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="interface">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates select="descendant::Foundation.Core.Operation"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- attribute -->
	<xsl:template match="Foundation.Core.Attribute">
		<menu.Node type="attribute">
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
		</menu.Node>
	</xsl:template>

	<!-- operation -->
	<xsl:template match="Foundation.Core.Operation">
		<menu.Node type="operation">
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
		</menu.Node>
	</xsl:template>
	
	<!-- association -->
	<xsl:template match="Foundation.Core.Association">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="association">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- generalization -->
	<xsl:template match="Foundation.Core.Generalization">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="generalization">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- abstraction -->
	<xsl:template match="Foundation.Core.Abstraction">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="abstraction">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- use cases actor -->
	<xsl:template match="Behavioral_Elements.Use_Cases.Actor">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="actor">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- use case -->
	<xsl:template match="Behavioral_Elements.Use_Cases.UseCase">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="use_case">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>		


	<!-- Sequence diagrams -->
	<!-- generic object -->
	<xsl:template match="Behavioral_Elements.Common_Behavior.Object">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="object">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- link -->
	<xsl:template match="Behavioral_Elements.Common_Behavior.Link">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="link">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- stimulus -->
	<xsl:template match="Behavioral_Elements.Common_Behavior.Stimulus">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="stimulus">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- CallAction -->
	<xsl:template match="Behavioral_Elements.Common_Behavior.CallAction">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="CallAction">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>


	<!-- Activity Diagram -->
	<xsl:template match="Behavioral_Elements.Activity_Graphs.ActivityGraph">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.ModelManagement>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.ModelManagement>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- activity -->
	<xsl:template match="Behavioral_Elements.Activity_Graphs.ActionState">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="activity">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- synchronization bar -->
	<xsl:template match="Behavioral_Elements.State_Machines.Pseudostate">
		<xsl:if test="string-length(@xmi.uuid) > 1">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="synchronization_bar">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.Node>
			</xsl:if>
		</xsl:if>
	</xsl:template>

	<!-- transition -->
	<xsl:template match="Behavioral_Elements.State_Machines.Transition">
		<xsl:if test="string-length(@xmi.uuid) > 0">
			<xsl:if test="contains(/XMI/XMI.uuidrefs/@values,concat(',',@xmi.uuid,','))">
				<menu.Node type="transition">
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
					<xsl:apply-templates/>
				</menu.Node>
			</xsl:if>	
		</xsl:if>
	</xsl:template>

	<xsl:template match="Foundation.Core.ModelElement.name">
		<xsl:attribute name="name">
			<xsl:value-of select="text()"/>
		</xsl:attribute>	
	</xsl:template>	

	<!--	attributes "xmi.id" and "xmi.uuid" renamed as "xmiId" and "xmiUuid":
		Java doesn't allow methods like "setXmi.id(..)" -->
	<xsl:template match="@xmi.id">
		<xsl:attribute name="xmiId">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>
	<xsl:template match="@xmi.uuid">
		<xsl:attribute name="xmiUuid">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@*">
		<xsl:copy />
	</xsl:template>

	<xsl:template match="text()">
	</xsl:template>

</xsl:stylesheet>