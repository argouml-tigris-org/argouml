<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

<xsl:key
    name="classifier"
    match="//Foundation.Core.Class|//Foundation.Core.Interface|
           //Foundation.Core.DataType"
    use="@xmi.id"/>

<xsl:key
    name="generalization"
    match="//Foundation.Core.Generalization"
    use="@xmi.id"/>

<xsl:key
    name="method_body"
    match="//Foundation.Core.Method"
    use="@xmi.id"/>

<xsl:key
    name="multiplicity"
    match="//Foundation.Data_Types.Multiplicity"
    use="@xmi.id"/>

<xsl:template match="XMI">
	<xsl:apply-templates select="//Model_Management.Model"/>
</xsl:template>

 <!-- Model -->
<xsl:template match="Model_Management.Model">
	<UML.Code.Source>
		<!-- xmi.uuid -->
		<xsl:apply-templates select="@xmi.uuid"/>

		<!-- Classes -->
		<xsl:apply-templates
			select="descendant::Foundation.Core.Class[@xmi.id]">
			<xsl:sort select="Foundation.Core.ModelElement.name"/>
		</xsl:apply-templates>

		<!-- Interfaces -->
		<xsl:apply-templates
			select="descendant::Foundation.Core.Interface[@xmi.id]">
			<xsl:sort select="Foundation.Core.ModelElement.name"/>
		</xsl:apply-templates>
	</UML.Code.Source>
</xsl:template>


<!-- Class -->
<xsl:template match="Foundation.Core.Class">
	<UML.Code.Class>
		<!-- xmi.uuid -->
		<xsl:apply-templates select="@xmi.uuid"/>

		<!-- name -->
		<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>

		<!-- visibility -->
		<xsl:apply-templates select="Foundation.Core.ModelElement.visibility"/>

		<!-- if abstract -->
		<xsl:apply-templates select="Foundation.Core.GeneralizableElement.isAbstract"/>

		<!-- supertypes (classes and interfaces) -->						
		<xsl:call-template name="supertypes"/>

		<!-- attributes -->
		<xsl:call-template name="attributes"/>

		<!-- operations -->
		<xsl:call-template name="operations"/>

		<!-- imported packages -->
		<xsl:call-template name="packages"/>
	</UML.Code.Class>
</xsl:template>

<!-- Interface -->
<xsl:template match="Foundation.Core.Interface">
	<UML.Code.Interface>
		<!-- xmi.uuid -->
		<xsl:apply-templates select="@xmi.uuid"/>

		<!-- name -->
		<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>

		<!-- visibility -->
		<xsl:apply-templates select="Foundation.Core.ModelElement.visibility"/>

		<!-- supertypes (classes and interfaces) -->						
		<xsl:call-template name="supertypes"/>

		<!-- operations -->
		<xsl:call-template name="operations"/>

		<!-- imported packages -->
		<xsl:call-template name="packages"/>
	</UML.Code.Interface>
</xsl:template>


<!-- Supertypes (inheritance) -->
<xsl:template name="supertypes">
	<!-- Generalizations identify supertypes -->
	<xsl:variable name="generalizations"
		select="Foundation.Core.GeneralizableElement.generalization/Foundation.Core.Generalization"/>

	<xsl:if test="count($generalizations) > 0">
		<xsl:for-each select="$generalizations">
			<UML.Code.Generalization>

				<!-- get the parent in the generalization -->
				<xsl:variable name="generalization"
						select="key('generalization', ./@xmi.idref)" />
				<xsl:variable name="target"
					select="$generalization/Foundation.Core.Generalization.parent/*/@xmi.idref" />
				
				<xsl:call-template name="classify_type">
					<xsl:with-param name="target" select="$target"/>
				</xsl:call-template>

				<xsl:call-template name="classify_name">
					<xsl:with-param name="target" select="$target"/>
				</xsl:call-template>

			</UML.Code.Generalization>
		</xsl:for-each>
	</xsl:if>
</xsl:template>


<!-- Attributes -->
<xsl:template name="attributes">
	<xsl:variable name="attributes"
		select="Foundation.Core.Classifier.feature/Foundation.Core.Attribute"/>
    
	<xsl:if test="count($attributes) > 0">
		<xsl:for-each select="$attributes">
			<UML.Code.Attribute>
				<xsl:variable name="target"
						  select='Foundation.Core.StructuralFeature.type/*/@xmi.idref'/>

				<!-- name -->
				<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>

				<!-- type -->
				<xsl:call-template name="classify_type">
					<xsl:with-param name="target" select="$target" />
				</xsl:call-template>
				<xsl:call-template name="classify_name">
					<xsl:with-param name="target" select="$target"/>
				</xsl:call-template>

				<!-- visibility -->
				<xsl:apply-templates select="Foundation.Core.ModelElement.visibility"/>

				<!-- multiplicity -->
				<xsl:apply-templates select=".//Foundation.Data_Types.Multiplicity" />

				<!-- if static -->
				<xsl:apply-templates select="Foundation.Core.Feature.ownerScope"/>

				<!-- if final -->
				<xsl:apply-templates select="Foundation.Core.StructuralFeature.changeability"/>

			</UML.Code.Attribute>
		</xsl:for-each>
	</xsl:if>	
</xsl:template>


<!-- Operations (Methods) -->
<xsl:template name="operations">
	<xsl:variable name="methods"
		select="Foundation.Core.Classifier.feature/Foundation.Core.Operation"/>
    
	<xsl:if test="count($methods) > 0">
		<xsl:for-each select="$methods">
			<UML.Code.Method.Method>

				<!-- method name -->
				<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>

				<!-- method visibility -->
				<xsl:apply-templates select="Foundation.Core.ModelElement.visibility"/>

				<!-- if abstract -->
				<xsl:apply-templates select="Foundation.Core.Operation.isAbstract"/>

				<!-- method parameters -->
				<xsl:call-template name="parameters"/>
				
				<!-- method body -->
				<xsl:variable name="body_idref"
					  select="Foundation.Core.Operation.method/Foundation.Core.Method/@xmi.idref" />
				<xsl:call-template name="method_body">
					<xsl:with-param name="body_idref" select="$body_idref"/>
				</xsl:call-template>
			</UML.Code.Method.Method>
		</xsl:for-each>
	</xsl:if>
</xsl:template>


<!-- Parameters of a method -->
<xsl:template name="parameters">
	<xsl:variable name="parameters"
			  select="Foundation.Core.BehavioralFeature.parameter/Foundation.Core.Parameter" />

	<xsl:if test="count($parameters) > 0">
		<xsl:for-each select="$parameters">
			<xsl:variable name="parameter" select="."/>
			<xsl:variable name="target"
					  select="$parameter/Foundation.Core.Parameter.type/*/@xmi.idref" />
			<UML.Code.Method.Parameter>
				<!-- parameter name -->
				<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
				
				<!-- parameter kind (input or return) -->
				<xsl:attribute name="kind">
					<xsl:value-of select="$parameter/Foundation.Core.Parameter.kind/@xmi.value"/>
				</xsl:attribute>

				<!-- parameter type -->
				<xsl:choose>
					<!-- void -->
					<xsl:when test="string-length($target) = 0">
						<xsl:attribute name="type">void</xsl:attribute>
					</xsl:when>
	
					<!-- not void (class, interface or primitive type) -->
					<xsl:otherwise>
						<xsl:call-template name="classify_name">
							<xsl:with-param name="target" select="$target"/>
						</xsl:call-template>
					</xsl:otherwise>
				</xsl:choose>
			</UML.Code.Method.Parameter>
		</xsl:for-each>
	</xsl:if>
</xsl:template>

<!-- Method body -->
<xsl:template name="method_body">
	<xsl:param name="body_idref"/>

	<!-- get the reference to the method body -->
	<xsl:variable name="method_body"
			  select="key('method_body', $body_idref)" />

	<xsl:variable name="language"
			  select="$method_body/Foundation.Core.Method.body/Foundation.Data_Types.ProcedureExpression/Foundation.Data_Types.Expression.language"/>

	<xsl:variable name="body"
			  select="$method_body/Foundation.Core.Method.body/Foundation.Data_Types.ProcedureExpression/Foundation.Data_Types.Expression.body"/>

	<xsl:if test="$language = 'Java'">
		<UML.Code.Method.Body>
			<xsl:value-of select="$body"/>
		</UML.Code.Method.Body>
	</xsl:if>
</xsl:template>

<!-- Classification Type -->
<xsl:template name="classify_type">
	<xsl:param name="target"/>

	<xsl:variable name="classifier"
			  select="key('classifier', $target)" />
                  
	<xsl:variable name="classifier_name"
			  select="$classifier/Foundation.Core.ModelElement.name" />

	<xsl:variable name="type" select="name($classifier)" />

	<!-- Get the type of the classifier (class, interface, datatype) -->
	<xsl:variable name="classifier_type">
		<xsl:choose>
			<xsl:when test="$type='Foundation.Core.Class'">class</xsl:when>
			<xsl:when test="$type='Foundation.Core.Interface'">interface</xsl:when>
			<xsl:when test="$type='Foundation.Core.DataType'">datatype</xsl:when>
			<xsl:otherwise>classifier</xsl:otherwise>
		</xsl:choose>
	</xsl:variable>
    
	<xsl:attribute name="type">
		<xsl:value-of select="$classifier_type"/>
	</xsl:attribute>

</xsl:template>

<!-- Classification Name -->
<xsl:template name="classify_name">
	<xsl:param name="target"/>

	<xsl:variable name="classifier"
			  select="key('classifier', $target)" />
    
	<xsl:attribute name="typename">
		<xsl:value-of select="$classifier/Foundation.Core.ModelElement.name"/>
	</xsl:attribute>

</xsl:template>

<!-- Imported packages for a class or interface -->
<xsl:template name="packages">
	<xsl:variable name="packages"
		select="//Model_Management.Model/Foundation.Core.Namespace.ownedElement/Model_Management.Package"/>

	<xsl:if test="count($packages) > 0">
		<xsl:apply-templates select="$packages"/>	
	</xsl:if>
</xsl:template>

<xsl:template match="Model_Management.Package">
	<UML.Code.Package>
		<xsl:apply-templates select="Foundation.Core.ModelElement.name"/>
		<xsl:apply-templates select="Foundation.Core.Namespace.ownedElement/Model_Management.Package"/>	
	</UML.Code.Package>
</xsl:template>


<!-- abstract class -->
<xsl:template match="Foundation.Core.GeneralizableElement.isAbstract">
	<xsl:attribute name="isAbstract">
		<xsl:value-of select="@xmi.value"/>
	</xsl:attribute>	
</xsl:template>

<!-- abstract method -->
<xsl:template match="Foundation.Core.Operation.isAbstract">
	<xsl:attribute name="isAbstract">
		<xsl:value-of select="@xmi.value"/>
	</xsl:attribute>	
</xsl:template>


<!-- generic element name (Class, interface, attribute, etc.) -->
<xsl:template match="Foundation.Core.ModelElement.name">
	<xsl:attribute name="name">
		<xsl:value-of select="text()"/>
	</xsl:attribute>	
</xsl:template>

<!-- class, interface or attribute visibility -->
<xsl:template match="Foundation.Core.ModelElement.visibility">
	<xsl:attribute name="visibility">
		<xsl:value-of select="@xmi.value"/>
	</xsl:attribute>	
</xsl:template>

<!-- static -->
<xsl:template match="Foundation.Core.Feature.ownerScope">
	<xsl:attribute name="ownerScope">
		<xsl:value-of select="@xmi.value"/>
	</xsl:attribute>	
</xsl:template>

<!-- final -->
<xsl:template match="Foundation.Core.StructuralFeature.changeability">
	<xsl:attribute name="changeability">
		<xsl:value-of select="@xmi.value"/>
	</xsl:attribute>	
</xsl:template>

<!-- Multiplicity (definition) -->
<xsl:template match="Foundation.Data_Types.Multiplicity[@xmi.id]">
	<xsl:variable name="lower"
			  select=".//Foundation.Data_Types.MultiplicityRange.lower"/>
	 
	<xsl:variable name="upper" 
			  select=".//Foundation.Data_Types.MultiplicityRange.upper"/>

    	<xsl:attribute name="multiplicity">
		<xsl:choose>
			<xsl:when test="$upper = $lower">
				<xsl:value-of select="$lower"/>
			</xsl:when>
			<xsl:otherwise>
				<xsl:value-of select="$lower"/>..<xsl:value-of select="$lower"/>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:attribute>
</xsl:template>

<!-- Multiplicity (reference) -->
<xsl:template match="Foundation.Data_Types.Multiplicity[@xmi.idref]">
	<xsl:apply-templates select="key('multiplicity', @xmi.idref)" />
</xsl:template>

<xsl:template match="@xmi.uuid">
	<xsl:attribute name="xmiUuid">
		<xsl:value-of select="."/>
	</xsl:attribute>
</xsl:template>

</xsl:stylesheet>