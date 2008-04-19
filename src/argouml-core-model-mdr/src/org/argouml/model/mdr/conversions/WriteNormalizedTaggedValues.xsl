<?xml version = '1.0' encoding = 'ISO-8859-1' ?>
<!-- Original Contribution by Dave Carlson (dcarlson@ontogenics.com) -->
<!-- Modified by Roy Feldman (roy@truehorizon.com) -->
<!-- Please send all corrections or additions to the MDR mail list users@mdr.netbeans.org -->
<xsl:stylesheet 
    xmlns:xsl = "http://www.w3.org/1999/XSL/Transform"
    xmlns:xalan="http://xml.apache.org/xslt"
    xmlns:saxon = "http://icl.com/saxon"
    extension-element-prefixes = "xalan saxon"
    exclude-result-prefixes = "xalan saxon"
    version = "1.0" >  
    <xsl:preserve-space elements="*"/>
  <!-- 
    * Write out all  TaggedValue elements.
   -->
  <xsl:template name="writeTaggedValues-uml1.3">
    <xsl:for-each select="//Foundation.Extension_Mechanisms.TaggedValue">
      <xsl:variable name="tag">
        <xsl:choose>
          <xsl:when test="starts-with(Foundation.Extension_Mechanisms.TaggedValue.tag, 'RationalRose$')">
            <xsl:value-of select="substring-after(Foundation.Extension_Mechanisms.TaggedValue.tag, ':')"/>
          </xsl:when>
          <xsl:otherwise>
            <xsl:value-of select="Foundation.Extension_Mechanisms.TaggedValue.tag"/>
          </xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <xsl:variable name="value">
        <xsl:choose>
          <!-- Rose outputs all boolean tag values with upper case first letter -->
          <xsl:when test="Foundation.Extension_Mechanisms.TaggedValue.value = 'True'">true</xsl:when>
          <xsl:when test="Foundation.Extension_Mechanisms.TaggedValue.value = 'False'">false</xsl:when>
          <xsl:otherwise><xsl:value-of select="Foundation.Extension_Mechanisms.TaggedValue.value"/></xsl:otherwise>
        </xsl:choose>
      </xsl:variable>
      <!-- (1) Ignore TaggedValues starting with "RationalRose:Tool#" -->
      <!--     This tag is on the Model element in Rose exports -->
      <!-- (2) Ignore TaggedValues with empty values -->
      <!--     This is useful for Visio, which creates an empty 'documentation' tag for every ModelElement -->
      <xsl:if test="not(starts-with($tag, 'RationalRose:Tool#'))
      				and string-length($value) > 0">
        <xsl:choose>
          <!-- xmi.id is not mandatory, so check if it exists - issue 4074 -->
          <xsl:when test="@xmi.id">
            <Foundation.Extension_Mechanisms.TaggedValue xmi.id="{@xmi.id}"> 
              <Foundation.Extension_Mechanisms.TaggedValue.tag><xsl:value-of select="$tag"/></Foundation.Extension_Mechanisms.TaggedValue.tag>
              <Foundation.Extension_Mechanisms.TaggedValue.value><xsl:value-of select="$value"/></Foundation.Extension_Mechanisms.TaggedValue.value>
              <Foundation.Extension_Mechanisms.TaggedValue.modelElement>
                <Foundation.Core.ModelElement xmi.idref="{*/*/@xmi.idref}"/>
              </Foundation.Extension_Mechanisms.TaggedValue.modelElement>
            </Foundation.Extension_Mechanisms.TaggedValue>
          </xsl:when>
          <xsl:otherwise>
            <Foundation.Extension_Mechanisms.TaggedValue>
              <Foundation.Extension_Mechanisms.TaggedValue.tag><xsl:value-of select="$tag"/></Foundation.Extension_Mechanisms.TaggedValue.tag>
              <Foundation.Extension_Mechanisms.TaggedValue.value><xsl:value-of select="$value"/></Foundation.Extension_Mechanisms.TaggedValue.value>
              <Foundation.Extension_Mechanisms.TaggedValue.modelElement>
                <Foundation.Core.ModelElement xmi.idref="{*/*/@xmi.idref}"/>
              </Foundation.Extension_Mechanisms.TaggedValue.modelElement>
            </Foundation.Extension_Mechanisms.TaggedValue>
          </xsl:otherwise>
        </xsl:choose>

      </xsl:if>
      
    </xsl:for-each>
  </xsl:template>
</xsl:stylesheet>
