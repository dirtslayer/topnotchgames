<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="2.0"
	xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
	xmlns:xhtml="http://www.w3.org/1999/xhtml"
	xmlns:fb="http://ogp.me/ns/fb#"
   exclude-result-prefixes="xhtml"
	>
<xsl:output method="html" omit-xml-declaration="yes" indent="no" >

</xsl:output>

<xsl:key name="group" match="/itemlist/item/group/text()" use="." />


<xsl:template match="itemlist" >
<xsl:text disable-output-escaping='yes'>&lt;!DOCTYPE html>
</xsl:text>
<html>
<head>
<title>TNG Catalogue</title>
<link rel="stylesheet" type="text/css" href="catalogue.css" />
<link href="http://fonts.googleapis.com/css?family=Archivo+Narrow" rel="stylesheet" type="text/css"/>
</head>
<body>
	<div id="header">
		<div class="logo" >
			<img src="/controller01.png" />
		<h1 >&#160;&#160;&#160;&#160;Top<br/>&#160;&#160;Notch<br/>Games</h1>
		<img class="rimg" src="/controller02.png" />
		</div>
		<div class="logo">
		<ul>
		<li>9934 - 103 Street Ft. Sask</li>
		<li>780.589.1155</li>
		</ul>
		</div>
		<div class="redlogo">
		<ul>
		<li>new</li>
		<li>used</li>
		<li>pre-order</li>
		<li>accessories</li>
		</ul>
		</div>
	</div>
	
	<div class="colmask leftmenu">
		<div class="colright">
			<div class="col1wrap">
				<div class="col1">

<a name="index" />
<ul>
<xsl:for-each select="/itemlist/item/group/text()[generate-id()=generate-id(key('group',.)[1])]">
    <xsl:sort></xsl:sort>
    <li>
    	<xsl:element name="a">
    	<xsl:attribute name="href">#<xsl:value-of select="."/></xsl:attribute>
    	<xsl:value-of select="."/>
    	</xsl:element>
      
    </li>
</xsl:for-each>
</ul>

<xsl:for-each select="/itemlist/item/group/text()[generate-id()=generate-id(key('group',.)[1])]">
<xsl:sort></xsl:sort>
	<xsl:variable name="vgroup" select="." />
	<xsl:element name="a">
	<xsl:attribute name="name">
	<xsl:value-of select="."/>
	</xsl:attribute>
	</xsl:element>
    <h2>
      <xsl:value-of select="."/>
    </h2>
<table>
<xsl:apply-templates select="/itemlist/item[group=$vgroup]">
	<xsl:sort select="name"></xsl:sort>
</xsl:apply-templates>
</table>    
<a href="#index">&lt;-----</a>
</xsl:for-each>



</div>
				<!-- col1  -->
			</div>
			<!-- colwrap -->
		
		<!-- colleft -->

		<div class="col2">
					

<ul>
<li>Email<br />
 topnotchgames<br/>
 @yahoo.com</li>				
</ul>

<ul >
<li><a href="/">Home</a></li>


<li><xsl:value-of select="count(//item)" /> items</li>

</ul>
<ul class="disclaimer">
<li class="disclaimer">new games will be available on the official release date</li>
<li class="disclaimer">Please call to confirm availability of a specific game</li>
<li class="disclaimer"><a href="http://designr8.com">designr8.com</a></li>
<li class="disclaimer"><a href="/gamefiler">game filer</a></li>
</ul>


		</div>
	</div>
	</div>
	<div id="footer">
		<div id="fb-root"></div>
		<script>
			(function(d, s, id) {
				var js, fjs = d.getElementsByTagName(s)[0];
				if (d.getElementById(id))
					return;
				js = d.createElement(s);
				js.id = id;
				js.src = "//connect.facebook.net/en_GB/all.js#xfbml=1";
				fjs.parentNode.insertBefore(js, fjs);
			}(document, 'script', 'facebook-jssdk'));
		</script>
		<fb:like href="http://www.topnotchgames.ca/" send="true"
			width="15em" show_faces="false"></fb:like>
		<div class="g-plusone" data-annotation="inline"></div>
		<script type="text/javascript">
			(function() {
				var po = document.createElement('script');
				po.type = 'text/javascript';
				po.async = true;
				po.src = 'https://apis.google.com/js/plusone.js';
				var s = document.getElementsByTagName('script')[0];
				s.parentNode.insertBefore(po, s);
			})();
		</script>
	</div>





</body>
</html>
</xsl:template>	
	
<xsl:template match="itemlist/item"><xsl:text>
</xsl:text>
<tr><td class="name"><xsl:element name="a"><xsl:attribute name="href">https://www.google.ca/search?q=<xsl:value-of select="name" />&amp;hl=en&amp;prmd=imvns&amp;source=lnms&amp;tbm=isch</xsl:attribute><xsl:value-of select="name" /></xsl:element></td><td class="price">$<xsl:value-of select="price" /></td></tr>
</xsl:template>
</xsl:stylesheet>
