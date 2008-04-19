<?xml version = '1.0' encoding = 'ISO-8859-1' ?>
<!-- Original Contribution by Dave Carlson (dcarlson@ontogenics.com) -->
<!-- Modified by Roy Feldman (roy@truehorizon.com) -->
<!-- Please send all corrections or additions to the MDR mail list users@mdr.netbeans.org -->
<!-- and the OpenModel mail list dev@openmodel.tigris.org -->

<xsl:stylesheet 
    xmlns:xsl = "http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:saxon = "http://icl.com/saxon"
    extension-element-prefixes = "xalan saxon"
    exclude-result-prefixes = "xalan saxon"
    version = "1.0" > 

   <xsl:include href="NormalizeXMI.xsl"/>  
    
  <xsl:variable name="exporter" select="/XMI/XMI.header/XMI.documentation/XMI.exporter"/>
    
    <!-- 
    * Remove invalid namespace child of Model (circularity error)
    *	I've see this once, but not in every case...
    * (nsuml 0.4.20)
   -->
  <xsl:template match="Model_Management.Model/Foundation.Core.ModelElement.namespace" >
    <!-- 
  	<xsl:message>Removing namespace child of Model</xsl:message>
    -->
  </xsl:template>

    <!-- 
    * Remove elements which represent the "wrong" end of a composition association
    * since they'll be inferred by nesting in their containing parent.  Having the
    * association represented twice is just an opportunity for them to not match.
   -->
  <xsl:template match="Foundation.Core.ModelElement.namespace" />
  <xsl:template match="Behavioral_Elements.State_Machines.State.stateMachine" />
  <xsl:template match="Behavioral_Elements.State_Machines.StateVertex.container" />

  <!-- 
    * Remove invalid embedded TaggedValues within a ModelElement.
    * (nsuml 0.4.20)
   -->
  <xsl:template match="Foundation.Core.ModelElement.taggedValue" >
    <!-- 
  	<xsl:message>Removing embedded TaggedValue</xsl:message>
    -->
  </xsl:template>
  
  
  <!-- 
    * Remove unnecessary child element of AssociationEnd
    *	it may not be necessary to remove this; what's correct?
    * (nsuml 0.4.20)
   -->
  <xsl:template match="Foundation.Core.AssociationEnd.association" >
    <!-- 
  	<xsl:message>Removing Foundation.Core.AssociationEnd.association</xsl:message>
    -->
  </xsl:template>
  
  <!-- 
    * Check for Multiplicity with xmi.idref
    * (nsuml 0.4.20)
   -->
  <xsl:template match="Foundation.Data_Types.Multiplicity[@xmi.idref]">
    <Foundation.Data_Types.Multiplicity xmi.id="{generate-id()}">
      <Foundation.Data_Types.Multiplicity.range>
        <Foundation.Data_Types.MultiplicityRange xmi.id="{generate-id()}_range">
          <xsl:copy-of select="key('xmi.id', @xmi.idref)/*/*/*" />
        </Foundation.Data_Types.MultiplicityRange>
      </Foundation.Data_Types.Multiplicity.range>
    </Foundation.Data_Types.Multiplicity>
  	<xsl:comment> copied from <xsl:value-of select="@xmi.idref"/> </xsl:comment>
  </xsl:template>
  
  <!-- 
    * Swap Base and Addition for Use Case Includes
    * (this affects the version of NSUML used by ArgoUML,
    * but it's unknown what other versions it affects)
    -->
  <xsl:template match="Behavioral_Elements.Use_Cases.Include.addition">
    <xsl:choose>
      <xsl:when test="$exporter='Novosoft UML Library'">
    <Behavioral_Elements.Use_Cases.Include.base>
      <xsl:copy-of select="*"/>
    </Behavioral_Elements.Use_Cases.Include.base>    
      </xsl:when>
      <xsl:otherwise>
        <Behavioral_Elements.Use_Cases.Include.addition>
          <xsl:copy-of select="*"/>
        </Behavioral_Elements.Use_Cases.Include.addition>    
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <xsl:template match="Behavioral_Elements.Use_Cases.Include.base">
    <xsl:choose>
      <xsl:when test="$exporter='Novosoft UML Library'">
    <Behavioral_Elements.Use_Cases.Include.addition>
      <xsl:copy-of select="*"/>
    </Behavioral_Elements.Use_Cases.Include.addition>    
      </xsl:when>
      <xsl:otherwise>
        <Behavioral_Elements.Use_Cases.Include.base>
          <xsl:copy-of select="*"/>
        </Behavioral_Elements.Use_Cases.Include.base>    
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

</xsl:stylesheet>

