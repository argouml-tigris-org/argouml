<?xml version="1.0"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

	<xsl:template match="svg">
		<svg.SVGPanel>
			<xsl:apply-templates select="@*"/>
			<xsl:apply-templates/>
		</svg.SVGPanel>
	</xsl:template>

	<xsl:template match="g">
		<xsl:choose>
			<xsl:when test="@type='Package'">
				<svg.groups.static_structure.SVGPackageGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.static_structure.SVGPackageGroup>
			</xsl:when>
			<xsl:when test="@type='Class'">
				<svg.groups.static_structure.SVGClassGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.static_structure.SVGClassGroup>
			</xsl:when>
			<xsl:when test="@type='Interface'">
				<svg.groups.static_structure.SVGInterfaceGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.static_structure.SVGInterfaceGroup>
			</xsl:when>
			<xsl:when test="@type='Association'">
				<svg.groups.static_structure.SVGAssociationGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.static_structure.SVGAssociationGroup>
			</xsl:when>
			<xsl:when test="@type='Realization'">
				<svg.groups.static_structure.SVGRealizationGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.static_structure.SVGRealizationGroup>
			</xsl:when>
			<xsl:when test="@type='Generalization'">
				<svg.groups.static_structure.SVGGeneralizationGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.static_structure.SVGGeneralizationGroup>
			</xsl:when>
			<xsl:when test="@type='Note'">
				<svg.groups.static_structure.SVGNoteGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.static_structure.SVGNoteGroup>
			</xsl:when>
			<xsl:when test="@type='Actor'">
				<svg.groups.use_cases.SVGActorGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.use_cases.SVGActorGroup>
			</xsl:when>
			<xsl:when test="@type='UseCase'">
				<svg.groups.use_cases.SVGUseCaseGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.use_cases.SVGUseCaseGroup>
			</xsl:when>
			<xsl:when test="@type='Object'">
				<svg.groups.sequence.SVGObjectGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.sequence.SVGObjectGroup>
			</xsl:when>
			<xsl:when test="@type='Link'">
				<svg.groups.sequence.SVGLinkGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.sequence.SVGLinkGroup>
			</xsl:when>
			<xsl:when test="@type='Stimulus'">
				<svg.groups.sequence.SVGStimulusGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.sequence.SVGStimulusGroup>
			</xsl:when>
			<xsl:otherwise>
				<svg.groups.SVGGroup>
					<xsl:apply-templates select="@*"/>
					<xsl:apply-templates/>
				</svg.groups.SVGGroup>
			</xsl:otherwise>
		</xsl:choose>
	</xsl:template>

	<xsl:template match="rect">
		<svg.graphic.SVGRect>
			<xsl:apply-templates select="@x|@y|@height|@width|@rx|@ry|@style"/>
		</svg.graphic.SVGRect>
	</xsl:template>

	<xsl:template match="text">
		<svg.graphic.SVGText>
			<xsl:apply-templates select="@x|@y|@style|text()"/>
		</svg.graphic.SVGText>
	</xsl:template>

	<xsl:template match="line">
		<svg.graphic.SVGLine>
			<xsl:apply-templates select="@x1|@y1|@x2|@y2|@style"/>
		</svg.graphic.SVGLine>
	</xsl:template>

	<xsl:template match="polyline">
		<svg.graphic.SVGPolyline>
			<xsl:apply-templates select="@points|@style"/>
		</svg.graphic.SVGPolyline>
	</xsl:template>

	<xsl:template match="polygon">
		<svg.graphic.SVGPolygon>
			<xsl:apply-templates select="@points|@style"/>
		</svg.graphic.SVGPolygon>
	</xsl:template>

	<xsl:template match="ellipse">
		<svg.graphic.SVGEllipse>
			<xsl:apply-templates select="@cx|@cy|@rx|@ry|@style"/>
		</svg.graphic.SVGEllipse>
	</xsl:template>

  <xsl:template match="text()">
  	<xsl:copy /> 
  </xsl:template>
  
  <xsl:template match="@*">
  	<xsl:copy /> 
  </xsl:template>

  </xsl:stylesheet>