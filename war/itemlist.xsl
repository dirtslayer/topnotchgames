<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
   exclude-result-prefixes="xhtml"
	>
<xsl:output method="html" omit-xml-declaration="yes" >

</xsl:output>
<xsl:template match="itemlist" >
<xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
<html>
<head>
<title>Import Results</title>
<link rel="stylesheet" type="text/css" href="itemlist.css" />
<link href="http://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet" type="text/css"/>
</head>
<body>
<h1>Import Results</h1>
<table>
<xsl:apply-templates/>
</table>
</body>
</html>
</xsl:template>	
	
<xsl:template match="itemlist/item">
<tr>
<td class="category"> <xsl:value-of select="category" /> </td>
<td class="group"> <xsl:value-of select="group" /> </td>
<td class="name"> <xsl:value-of select="name" /></td>
<td class="quantity"> <xsl:value-of select="quantity" /></td>
<td class="price">$<xsl:value-of select="price" /></td>
</tr>
</xsl:template>
</xsl:stylesheet>
