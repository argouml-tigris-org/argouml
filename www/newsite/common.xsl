<?xml version="1.0"?>

<xsl:stylesheet version="1.0" 
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">

<xsl:template match="page">
    <html>
    <head>
        <meta http-equiv="content-type" content="text/html; charset=ISO-8859-1" />
        <title><xsl:value-of select="@title"/></title>
        <style>
         
          A.header:link {color: white; text-decoration: none}
          A.header:active {color: red; text-decoration: none}
          A.header:visited {color: gray100; text-decoration: none}
         
        </style>
    </head>
     <body style="font-family: verdana, tahoma, helvetica, sans-serif; font-size: x-small" text="#000000" bgcolor="#f0f0f0" link="#0000ee" vlink="#551a8b" alink="#0000ee">
      <xsl:call-template name="header"/>
      <table cellpadding="3" cellspacing="0" border="0" width="100%">
  <tbody>
    <tr valign="Top">
      <td valign="Top">
      <table cellpadding="0" cellspacing="0" border="0" width="100%">
        <tbody>
          <tr valign="Top">
            <td valign="Top">
             <xsl:apply-templates select="section"/>
            </td>
            <td valign="Top" width="10"><br />
            </td>
            <td valign="Top" width="30%">
             <xsl:apply-templates select="leftboxes"/>
            </td>
          </tr>
        </tbody>
       </table>
      </td>
      </tr>
      </tbody>
      </table>
     </body>
    </html>
</xsl:template>

<xsl:template match="content">
    <font size="-2">
     <xsl:for-each select="//section|//subsection|//subsubsection">
      <xsl:element name="a">
        <xsl:attribute name="href"><xsl:text>#</xsl:text><xsl:value-of select="position()"/></xsl:attribute>
       <xsl:value-of select="@title"/><br />
      </xsl:element>
     </xsl:for-each>
     </font>
</xsl:template>


<xsl:template match="section">
    <xsl:element name="a">
     <xsl:attribute name="name">
       <xsl:value-of select="position()"/>
     </xsl:attribute>
    </xsl:element>
    <h1><xsl:value-of select="@title"/></h1>
    <xsl:apply-templates/>
</xsl:template>

<xsl:template match="subsection">
    <xsl:element name="a">
     <xsl:attribute name="name">
       <xsl:value-of select="position()"/>
     </xsl:attribute>
    </xsl:element>
    <h2><xsl:value-of select="@title"/></h2>
    <xsl:apply-templates/>
</xsl:template>

<xsl:template match="subsubsection">
  <xsl:element name="a">
     <xsl:attribute name="name">
       <xsl:value-of select="position()"/>
     </xsl:attribute>
    </xsl:element>
  <h3><xsl:value-of select="@title"/></h3> 
  <xsl:apply-templates/> 
</xsl:template>

<xsl:template match="leftboxes">
 <xsl:apply-templates/>
</xsl:template>

<xsl:template match="leftbox">
<table cellpadding="3" cellspacing="0" border="1" width="100%">
              <tbody>
                <tr valign="Top">
                  <td valign="Top" bgcolor="#3366ff">
                   <font face="Helvetica, Arial, sans-serif" color="#ffffff">
                    <b>
                     <xsl:value-of select="@title"/>
                    </b>
                   </font>
                  </td>
                </tr>
                <tr valign="Top">
                  <td valign="Top" bgcolor="#ffffff">
                   <font face="Helvetica, Arial, sans-serif">
                   <font size="-1">
                    <xsl:apply-templates/>
                   </font>
                   </font>
                  </td>
                 </tr>
               </tbody>
</table>
<br />
</xsl:template>

<xsl:template name="header">

<div align="Right"><br />
<table cellpadding="0" cellspacing="0" border="0" width="100%" background="background.gif">
  <tbody>
    <tr valign="Top">
      <td valign="Top"><img alt="Logo" src="/argouml/newsite/images/argologo_small_lines.gif" /><br />
      </td>
    </tr>
  </tbody>
</table>
</div>
<table cellpadding="3" cellspacing="0" border="0" width="100%">
  <tbody>
    <tr valign="Top">
      <td valign="Top" bgcolor="#000099"><font size="+3" color="#ffffff"><b><font face="Helvetica, Arial, sans-serif"><xsl:value-of select="@title"/></font></b></font></td>
    </tr>
    <tr valign="Top">
      <td valign="Top" bgcolor="#3366ff"><b><font face="Helvetica, Arial, sans-serif" color="#ffffff">
<!--
home | documentation | people | download | screenshots| news | uml resources
| bugs

--> 
        <xsl:apply-templates select="topnavbar"/>
      </font></b></td> 
    </tr>
    <tr valign="Top">
      <td valign="Top"><br />
      </td>
    </tr>
  </tbody>
</table>

</xsl:template>

<xsl:template match="people/person">
   <h2><xsl:value-of select="@name"/></h2>
   <img>
    <xsl:attribute name="src"><xsl:value-of select="image"/></xsl:attribute>
    <xsl:attribute name="align">left</xsl:attribute>
    <xsl:attribute name="hspace">20</xsl:attribute>
    <xsl:attribute name="vspace">20</xsl:attribute>
   </img>
   <xsl:apply-templates select="para"/> 
   <hr/>
</xsl:template>

<xsl:template match="topnavbar">
    <xsl:for-each select="navbaritem">
     <a class="header">
       <xsl:attribute name="href"><xsl:value-of select="."/></xsl:attribute>
       <xsl:value-of select="@name"/>
     </a>
     <xsl:choose>
      <xsl:when test="position() &lt; last()">
       <xsl:text> | </xsl:text>
      </xsl:when>
      <xsl:otherwise>
      </xsl:otherwise>
     </xsl:choose>
    </xsl:for-each>
</xsl:template>

<xsl:template match="para">
    <p>
        <xsl:apply-templates/>
    </p>
</xsl:template>


<xsl:template match="img">
     <xsl:copy-of select=".|@*"/>
</xsl:template>

<xsl:template match="a">
     <xsl:copy-of select=".|@*"/>
</xsl:template>

<xsl:template match="ul">
     <xsl:copy-of select=".|@*"/>
</xsl:template>

</xsl:stylesheet>







