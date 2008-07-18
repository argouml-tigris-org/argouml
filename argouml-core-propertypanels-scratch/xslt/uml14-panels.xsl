<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:Model='omg.org/mof.Model/1.3'>

	<xsl:output method="xml" indent="yes" />

	<xsl:template
		match="/XMI/XMI.content/Model:Package[@name='Core']/Model:Namespace.contents">
		<panels>
			<xsl:for-each select="Model:Class">
				<panel name="{@name}">
					<!-- We take the references -->
					<xsl:for-each
						select="Model:Namespace.contents//Model:References">
						<xsl:variable name="datatypeRef" select="@type" />
						<xsl:variable name="datatype"
							select="//Model:Class[@xmi.id=$datatypeRef]/@name" />
						<xsl:variable name="multiplicityLower"
							select="Model:StructuralFeature.multiplicity/XMI.field[1]" />
						<xsl:variable name="multiplicityUpper"
							select="Model:StructuralFeature.multiplicity/XMI.field[2]" />
						<xsl:choose>
							<xsl:when test="$datatype = 'Namespace'">
								<combo name="{@name}" multiplicity="{$multiplicityUpper - $multiplicityLower}" />
							</xsl:when>
							<xsl:otherwise>
							</xsl:otherwise>							
						</xsl:choose>
					</xsl:for-each>
					<!-- We take the attributes -->
					<xsl:for-each
						select="Model:Namespace.contents//Model:Attribute">
						<xsl:variable name="datatypeRef" select="@type" />
						<xsl:variable name="datatype"
							select="//Model:DataType[@xmi.id=$datatypeRef]/@name" />
						<xsl:variable name="multiplicityLower"
							select="Model:StructuralFeature.multiplicity/XMI.field[1]" />
						<xsl:variable name="multiplicityUpper"
							select="Model:StructuralFeature.multiplicity/XMI.field[2]" />
						<xsl:choose>
							<xsl:when test="$datatype = 'Boolean'">
								<checkbox name="{@name}"
									type="{$datatype}"
									multiplicity="{$multiplicityUpper - $multiplicityLower}" />
							</xsl:when>
							<xsl:otherwise>
								<xsl:choose>
									<xsl:when
										test="$datatype = 'Name'">
										<text name="{@name}" />
									</xsl:when>
									<xsl:otherwise>
										<xsl:choose>
											<xsl:when
												test="$multiplicityUpper  = $multiplicityLower">
												<singlerow
													name="{@name}" type="{$datatype}"
													multiplicity="{$multiplicityUpper - $multiplicityLower}" />
											</xsl:when>
											<xsl:when
												test="$multiplicityLower = -1">
												<list name="{@name}"
													type="{$datatype}"
													multiplicity="{$multiplicityUpper - $multiplicityLower}" />
											</xsl:when>
											<xsl:otherwise>
												<item name="{@name}"
													type="{$datatype}"
													multiplicity="{$multiplicityUpper - $multiplicityLower}" />
											</xsl:otherwise>
										</xsl:choose>
									</xsl:otherwise>
								</xsl:choose>
							</xsl:otherwise>
						</xsl:choose>
					</xsl:for-each>
				</panel>
			</xsl:for-each>
		</panels>
		<xsl:apply-templates />
	</xsl:template>

	<xsl:template match="@*|node()">
		<xsl:apply-templates />
	</xsl:template>

</xsl:stylesheet>