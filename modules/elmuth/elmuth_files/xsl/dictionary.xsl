<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">


 <!-- ///////////////////////////////////////////////////////////////////////
      // Root rule
      /////////////////////////////////////////////////////////////////////// -->
  	<!-- XMI -->

  <xsl:template match="XMI">
      <xsl:apply-templates select="XMI.content"/>
  </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Content
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="XMI.content">
		<XMI.Content>
			<xsl:apply-templates/>
		</XMI.Content>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // DataType
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.DataType">
		<UML.Semantic.Foundation.Core.DataType>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.DataType>
	 </xsl:template>


 <!-- ///////////////////////////////////////////////////////////////////////
      // Model
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Model_Management.Model">
		<UML.Semantic.ModelManagement.Model>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.ModelManagement.Model>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Package
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Model_Management.Package">
		<UML.Semantic.ModelManagement.Package>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.ModelManagement.Package>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // DataType
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.DataType">
		<UML.Semantic.Foundation.Core.DataType>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.DataType>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Interface
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Interface">
		<UML.Semantic.Foundation.Core.Interface>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Interface>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Class
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Class">
		<UML.Semantic.Foundation.Core.Class>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Class>
	 </xsl:template>

 <!-- namespace as attribute -->
 <!--<rule>
  <element type="Foundation.Core.ModelElement.namespace">
    <target-element>
      <attribute name="xmi.idref"/>
    </target-element>
  </element>
  <attribute name="namespace" value="=element.getAttribute('xmi.idref')"/>
 </rule>-->
	 <xsl:template match="Foundation.Core.ModelElement.namespace">
		<xsl:attribute name="namespace">
			<xsl:value-of select="@xmi.idref"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- generalization -->
 <!--<rule>
   <element type="Foundation.Core.GeneralizableElement.generalization">
     <target-element>
       <attribute name="xmi.idref"/>
     </target-element>
   </element>
   <element type="XMI.Reference">
     <attribute name="role" value="generalization"/>
     <attribute name="idref" value="=element.getAttribute('xmi.idref')"/>
   </element>
 </rule>-->
	 <xsl:template match="Foundation.Core.GeneralizableElement.generalization">
		<XMI.Reference>
			<xsl:attribute name="role">
				generalization
			</xsl:attribute>
			<xsl:attribute name="idref">
				<xsl:value-of select="@xmi.idref"/>
			</xsl:attribute>
		</XMI.Reference>
	 </xsl:template>

 <!-- specialization -->
 <!--<rule>
   <element type="Foundation.Core.GeneralizableElement.specialization">
     <target-element>
       <attribute name="xmi.idref"/>
     </target-element>
   </element>
   <element type="XMI.Reference">
     <attribute name="role" value="specialization"/>
     <attribute name="idref" value="=element.getAttribute('xmi.idref')"/>
   </element>
 </rule>-->
	 <xsl:template match="Foundation.Core.GeneralizableElement.generalization">
		<XMI.Reference>
			<xsl:attribute name="role">
				specialization
			</xsl:attribute>
			<xsl:attribute name="idref">
				<xsl:value-of select="@xmi.idref"/>
			</xsl:attribute>
		</XMI.Reference>
	 </xsl:template>


 <!-- association -->
 <!--<rule>
   <element type="Foundation.Core.Classifier.associationEnd">
     <target-element>
       <attribute name="xmi.idref"/>
     </target-element>
   </element>
   <element type="XMI.Reference">
     <attribute name="role" value="associationEnd"/>
     <attribute name="idref" value="=element.getAttribute('xmi.idref')"/>
   </element>
 </rule>-->
	 <xsl:template match="Foundation.Core.GeneralizableElement.generalization">
		<XMI.Reference>
			<xsl:attribute name="role">
				associationEnd
			</xsl:attribute>
			<xsl:attribute name="idref">
				<xsl:value-of select="@xmi.idref"/>
			</xsl:attribute>
		</XMI.Reference>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Attribute
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Attribute">
		<UML.Semantic.Foundation.Core.Attribute>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Attribute>
	 </xsl:template>

	 <!-- ownerScope as attribute -->
	 <xsl:template match="Foundation.Core.Feature.ownerScope">
		<xsl:attribute name="ownerScope">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 	<!-- multiplicity as attribute: TODO -->
	 <xsl:template match="Foundation.Core.StructuralFeature.multiplicity">
		<xsl:attribute name="multiplicity">
			<xsl:value-of select="text()"/>
		</xsl:attribute>
	 </xsl:template>

 	<!-- changeable as attribute -->
	 <xsl:template match="Foundation.Core.StructuralFeature.changeable">
		<xsl:attribute name="changeable">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 	<!-- targetScope as attribute -->
	 <xsl:template match="Foundation.Core.StructuralFeature.targetScope">
		<xsl:attribute name="targetScope">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- initial value as attribute -->
 <!--<rule>
  <element type="Foundation.Core.Attribute.initialValue">
    <element type="Foundation.Data_Types.Expression">
      <target-element type="Foundation.Data_Types.Expression.body"/>
    </element>
  </element>
  <attribute name="initialValue" value="=element.getText()"/>
 </rule>-->
	 <xsl:template match="Foundation.Core.Attribute.initialValue">
		<UML.Semantic.Foundation.DataTypes.Expression>
			<xsl:attribute name="initialValue">
				<xsl:value-of select="text()"/>
			</xsl:attribute>
			<xsl:apply-templates select="Foundation.Data_Types.Expression.body"/>
		</UML.Semantic.Foundation.DataTypes.Expression>
	 </xsl:template>

	 <!-- type as attribute -->
	 <xsl:template match="Foundation.Core.StructuralFeature.type">
		<xsl:attribute name="type">
			<xsl:value-of select="@xmi.idref"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- owner as attribute -->
	 <xsl:template match="Foundation.Core.Feature.owner">
		<xsl:attribute name="owner">
			<xsl:value-of select="@xmi.idref"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Operation
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Operation">
		<UML.Semantic.Foundation.Core.Operation>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Operation>
	 </xsl:template>

	 <!-- isQuery as attribute -->
	 <xsl:template match="Foundation.Core.BehavioralFeature.isQuery">
		<xsl:attribute name="isQuery">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- isPolymorphic as attribute -->
	 <xsl:template match="Foundation.Core.Operation.isPolymorphic">
		<xsl:attribute name="isPolymorphic">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- concurrency as attribute -->
	 <xsl:template match="Foundation.Core.Operation.concurrency">
		<xsl:attribute name="concurrency">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Method
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Method">
		<UML.Semantic.Foundation.Core.Method>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Method>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Parameter
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Parameter">
		<UML.Semantic.Foundation.Core.Parameter>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Parameter>
	 </xsl:template>

 <!-- default value as attribute -->
 <!--<rule>
  <element type="Foundation.Core.Attribute.defaultValue">
    <element type="Foundation.Data_Types.Expression">
      <target-element type="Foundation.Data_Types.Expression.body"/>
    </element>
  </element>
  <attribute name="initialValue" value="=element.getText()"/>
 </rule>-->
	 <xsl:template match="Foundation.Core.Attribute.defaultValue">
		<UML.Semantic.Foundation.DataTypes.Expression>
			<xsl:attribute name="initialValue">
				<xsl:value-of select="text()"/>
			</xsl:attribute>
		</UML.Semantic.Foundation.DataTypes.Expression>
	 </xsl:template>

	 <!-- kind as attribute -->
	 <xsl:template match="Foundation.Core.Parameter.kind">
		<xsl:attribute name="kind">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- type as attribute -->
 <!--<rule>
  <element type="Foundation.Core.Parameter.type">
    <target-element>
      <attribute name="xmi.idref"/>
    </target-element>
  </element>
  <attribute name="type" value="=element.getAttribute('xmi.idref')"/>
 </rule>-->
	 <xsl:template match="Foundation.Core.Parameter.type">
		<xsl:attribute name="type">
			<xsl:value-of select="Foundation.Core.Classifier/@xmi.idref"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Stereotype
      /////////////////////////////////////////////////////////////////////// -->
	<!-- TODO -->

 <!-- ///////////////////////////////////////////////////////////////////////
      // Generalization
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Generalization">
		<UML.Semantic.Foundation.Core.Generalization>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Generalization>
	 </xsl:template>

	 <!-- discriminator as attribute -->
	 <xsl:template match="Foundation.Core.Generalization.discriminator">
		<xsl:attribute name="discriminator">
			<xsl:value-of select="text()"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- subtype as attribute TODO-->
 <!--<rule>
   <element type="Foundation.Core.Generalization.subtype">
     <target-element>
       <attribute name="xmi.idref"/>
     </target-element>
   </element>
   <attribute name="subtype" value="=element.getAttribute('xmi.idref')"/>
 </rule>-->

 <!-- supertype as attribute TODO-->
 <!--<rule>
   <element type="Foundation.Core.Generalization.supertype">
     <target-element>
       <attribute name="xmi.idref"/>
     </target-element>
   </element>
   <attribute name="supertype" value="=element.getAttribute('xmi.idref')"/>
 </rule>-->

 <!-- ///////////////////////////////////////////////////////////////////////
      // Association
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Association">
		<UML.Semantic.Foundation.Core.Association>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.Association>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Association End
      /////////////////////////////////////////////////////////////////////// -->
 <!--<rule>
   <element type="Foundation.Core.Association.connection">
     <target-element type="Foundation.Core.AssociationEnd">
       <attribute name="xmi.id"/>
     </target-element>
   </element>
   <element type="UML.Semantic.Foundation.Core.AssociationEnd">
     <children/>
   </element>
 </rule>-->

	 <xsl:template match="Foundation.Core.AssociationEnd">
		<UML.Semantic.Foundation.Core.AssociationEnd>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates select="*"/>
		</UML.Semantic.Foundation.Core.AssociationEnd>
	 </xsl:template>

	 <!-- isNavigable as attribute -->
	 <xsl:template match="Foundation.Core.AssociationEnd.isNavigable">
		<xsl:attribute name="isNavigable">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- isOrdered as attribute -->
	 <xsl:template match="Foundation.Core.AssociationEnd.isOrdered">
		<xsl:attribute name="isOrdered">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- aggregation as attribute -->
	 <xsl:template match="Foundation.Core.AssociationEnd.aggregation">
		<xsl:attribute name="aggregation">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- multiplicity as attribute -->
	 <xsl:template match="Foundation.Core.AssociationEnd.multiplicity">
		<xsl:attribute name="multiplicity">
			<xsl:value-of select="text()"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- changeable as attribute -->
	 <xsl:template match="Foundation.Core.AssociationEnd.changeable">
		<xsl:attribute name="changeable">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

	 <!-- targetScope as attribute -->
	 <xsl:template match="Foundation.Core.AssociationEnd.targetScope">
		<xsl:attribute name="targetScope">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- type as attribute -->
 <!--<rule>
  <element type="Foundation.Core.AssociationEnd.type">
    <target-element>
      <attribute name="xmi.idref"/>
    </target-element>
  </element>
  <attribute name="type" value="=element.getAttribute('xmi.idref')"/>
 </rule>-->
	 <xsl:template match="Foundation.Core.AssociationEnd.type">
		<xsl:attribute name="type">
			<xsl:value-of select="Foundation.Core.Classifier/@xmi.idref"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- association as attribute -->
 <!--<rule>
  <element type="Foundation.Core.AssociationEnd.association">
    <target-element>
      <attribute name="xmi.idref"/>
    </target-element>
  </element>
  <attribute name="association" value="=element.getAttribute('xmi.idref')"/>
 </rule>-->
	 <xsl:template match="Foundation.Core.AssociationEnd.association">
		<xsl:attribute name="association">
			<xsl:value-of select="Foundation.Core.Association/@xmi.idref"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Name as attribute
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.ModelElement.name">
		<xsl:attribute name="name">
			<xsl:value-of select="text()"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // isRoot as attribute
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.GeneralizableElement.isRoot">
		<xsl:attribute name="isRoot">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // isLeaf as attribute
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.GeneralizableElement.isLeaf">
		<xsl:attribute name="isLeaf">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // isAbstract as attribute
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.GeneralizableElement.isAbstract">
		<xsl:attribute name="isAbstract">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // isActive as attribute
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.Class.isActive">
		<xsl:attribute name="isActive">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

 <!-- ///////////////////////////////////////////////////////////////////////
      // Visibility
      /////////////////////////////////////////////////////////////////////// -->
	 <xsl:template match="Foundation.Core.ModelElement.visibility">
		<xsl:attribute name="visibility">
			<xsl:value-of select="@xmi.value"/>
		</xsl:attribute>
	 </xsl:template>

<!-- ///////////////////////////////////////////////////////////////////////
     // Common attributes
     /////////////////////////////////////////////////////////////////////// -->
	<xsl:template match="@xmi.id">
		<xsl:attribute name="id">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@xmi.label">
		<xsl:attribute name="label">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@xmi.uuid">
		<xsl:attribute name="uuid">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@href">
		<xsl:attribute name="href">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@title">
		<xsl:attribute name="title">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@xmi.idref">
		<xsl:attribute name="idref">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@xmi.uuidref">
		<xsl:attribute name="uuidref">
			<xsl:value-of select="."/>
		</xsl:attribute>
	</xsl:template>

	<xsl:template match="@isOwned">
		<xsl:attribute name="isOwned">
			false
		</xsl:attribute>
	</xsl:template>

	<!--<xsl:template match="Foundation.Core.Namespace.ownedElement">
		<xsl:attribute name="isOwned">
			true
		</xsl:attribute>
	</xsl:template>-->

</xsl:stylesheet>



