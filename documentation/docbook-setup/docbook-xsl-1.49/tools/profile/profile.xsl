<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">

<!-- Generate DocBook instance with correct DOCTYPE -->
<xsl:output method="xml" 
            doctype-public="-//OASIS//DTD DocBook XML V4.1.2//EN"
            doctype-system="http://www.oasis-open.org/docbook/xml/4.0/docbookx.dtd"/>

<!-- Which OSes to select -->
<xsl:param name="os"/>

<!-- Which UserLevels to select -->
<xsl:param name="ul"/>

<!-- Which Archs to select -->
<xsl:param name="arch"/>

<!-- Name of attribute with profiling information -->
<xsl:param name="attr"/>

<!-- Which $attrs to select -->
<xsl:param name="val"/>

<!-- Seperator for profiling values -->
<xsl:param name="sep" select="';'"/>  

<!-- Copy all non-element nodes -->
<xsl:template match="@*|text()|comment()|processing-instruction()">
  <xsl:copy/>
</xsl:template>

<!-- Profile elements based on input parameters -->
<xsl:template match="*">
  <xsl:variable name="os.content">
    <xsl:if test="@os">
      <xsl:call-template name="cross.compare">
        <xsl:with-param name="a" select="$os"/>
        <xsl:with-param name="b" select="@os"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:variable>
  <xsl:variable name="os.ok" select="not(@os) or not($os) or
                                     $os.content != '' or @os = ''"/>

  <xsl:variable name="ul.content">
    <xsl:if test="@userlevel">
      <xsl:call-template name="cross.compare">
        <xsl:with-param name="a" select="$ul"/>
        <xsl:with-param name="b" select="@userlevel"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:variable>
  <xsl:variable name="ul.ok" select="not(@userlevel) or not($ul) or
                                     $ul.content != '' or @userlevel = ''"/>

  <xsl:variable name="arch.content">
    <xsl:if test="@arch">
      <xsl:call-template name="cross.compare">
        <xsl:with-param name="a" select="$arch"/>
        <xsl:with-param name="b" select="@arch"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:variable>
  <xsl:variable name="arch.ok" select="not(@arch) or not($arch) or
                                       $arch.content != '' or @arch = ''"/>

  <xsl:variable name="attr.content">
    <xsl:if test="@*[local-name()=$attr]">
      <xsl:call-template name="cross.compare">
        <xsl:with-param name="a" select="$val"/>
        <xsl:with-param name="b" select="@*[local-name()=$attr]"/>
      </xsl:call-template>
    </xsl:if>
  </xsl:variable>
  <xsl:variable name="attr.ok" select="not(@*[local-name()=$attr]) or not($val) or
                                       $attr.content != '' 
                                       or @*[local-name()=$attr] = '' or not($attr)"/>

  <xsl:if test="$os.ok and $ul.ok and $arch.ok and $attr.ok">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:if>
</xsl:template>

<!-- Returns non-empty string if list in $b contains one ore more values from list $a -->
<xsl:template name="cross.compare">
  <xsl:param name="a"/>
  <xsl:param name="b"/>
  <xsl:param name="head" select="substring-before(concat($a, $sep), $sep)"/>
  <xsl:param name="tail" select="substring-after($a, $sep)"/>
  <xsl:if test="contains(concat($sep, $b, $sep), concat($sep, $head, $sep))">1</xsl:if>
  <xsl:if test="$tail">
    <xsl:call-template name="cross.compare">
      <xsl:with-param name="a" select="$tail"/>
      <xsl:with-param name="b" select="$b"/>
    </xsl:call-template>
  </xsl:if>
</xsl:template>

</xsl:stylesheet>

