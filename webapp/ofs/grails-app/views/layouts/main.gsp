<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>

        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssreset/reset.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssbase/base.css" type="text/css"/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />

        <link rel="shortcut icon" href="${resource(dir:'images',file:'favicon.ico')}" type="image/x-icon" />
        <g:layoutHead />
        <g:javascript library="application" />

<script type="text/javascript">
  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', '${grailsApplication.config.ofs.analytics.code}']);
  _gaq.push(['_setDomainName', '.openfamilyservices.org.uk']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();
</script>

    </head>
    <body>
        <g:layoutBody />
    </body>
</html>
