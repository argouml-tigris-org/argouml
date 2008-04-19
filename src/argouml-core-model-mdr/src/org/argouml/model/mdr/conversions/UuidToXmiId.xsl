<?xml version="1.0" encoding="utf-8" ?>
<!-- $Id$
// Copyright (c) 2005 The Regents of the University of California. All
// Rights Reserved. Permission to use, copy, modify, and distribute this
// software and its documentation without fee, and without a written
// agreement is hereby granted, provided that the above copyright notice
// and this paragraph appear in all copies.  This software program and
// documentation are copyrighted by The Regents of the University of
// California. The software program and documentation are supplied "AS
// IS", without any accompanying services from The Regents. The Regents
// does not warrant that the operation of the program will be
// uninterrupted or error-free. The end-user understands that the program
// was developed for research purposes and is advised not to rely
// exclusively on the program for any reason.  IN NO EVENT SHALL THE
// UNIVERSITY OF CALIFORNIA BE LIABLE TO ANY PARTY FOR DIRECT, INDIRECT,
// SPECIAL, INCIDENTAL, OR CONSEQUENTIAL DAMAGES, INCLUDING LOST PROFITS,
// ARISING OUT OF THE USE OF THIS SOFTWARE AND ITS DOCUMENTATION, EVEN IF
// THE UNIVERSITY OF CALIFORNIA HAS BEEN ADVISED OF THE POSSIBILITY OF
// SUCH DAMAGE. THE UNIVERSITY OF CALIFORNIA SPECIFICALLY DISCLAIMS ANY
// WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
// MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE. THE SOFTWARE
// PROVIDED HEREUNDER IS ON AN "AS IS" BASIS, AND THE UNIVERSITY OF
// CALIFORNIA HAS NO OBLIGATIONS TO PROVIDE MAINTENANCE, SUPPORT,
// UPDATES, ENHANCEMENTS, OR MODIFICATIONS.

    These templates replace xmi.ids and their references with
    the value of the xmi.uuid attribute from the same element.
    This is required to support cross referencing from
    GEF's PGML files (used by ArgoUML) and is specific to
    that component
  -->

<xsl:stylesheet version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
>
  
  <xsl:key name="xmi.id-key" match="*" use="@xmi.id"/>
  
  <!-- update IDs -->
  <xsl:template match="@xmi.id">
    <xsl:variable name="uuid" select="../@xmi.uuid"/>
    <xsl:choose>
      <!-- if we have a UUID, change the xmi.id to its value -->
      <xsl:when test="$uuid">
         <xsl:attribute name="xmi.id">
            <xsl:value-of select="$uuid"/>
          </xsl:attribute>
      </xsl:when>
      <!-- otherwise just copy over the current value -->
      <xsl:otherwise>
        <xsl:copy>
          <xsl:apply-templates select="@*"/>
        </xsl:copy>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- update ID references to match -->
  <xsl:template match="@xmi.idref">
    <xsl:variable name="uuid" select="key('xmi.id-key',string(.))/@xmi.uuid"/>
    <xsl:choose>
      <!-- if we have a UUID, update the idref with it -->
      <xsl:when test="$uuid">
         <xsl:attribute name="xmi.idref">
            <xsl:value-of select="$uuid"/>
          </xsl:attribute>
      </xsl:when>
      <!-- otherwise just copy over the current value -->
      <xsl:otherwise>
        <xsl:copy>
          <xsl:apply-templates select="@*"/>
        </xsl:copy>
      </xsl:otherwise>
    </xsl:choose>
  </xsl:template>

  <!-- delete all the UUIDs since they aren't being used -->
  <xsl:template match="@xmi.uuid">
  </xsl:template>

</xsl:stylesheet>
