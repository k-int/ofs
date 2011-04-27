<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="Grails" /></title>

        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssreset/reset.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssbase/base.css" type="text/css"/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />

        <link rel="shortcut icon" href="${resource(dir:'images',file:'ofs_favicon.png')}" type="image/png" />
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

    <body class="search-results">
      <div class="yui3-g">
        <div class="yui3-u" style="width:100%">
          <div class="yui3-g">
            <div class="yui3-u" style="width:100%">
              <form action="/ofs/" method="get" >
                <div class="search-box">
                  <g:message code="ofs.search.prompt"/>  <input class="uiw-input" type="text" name="placename" value="${params.placename}" /><input class="uiw-button" value="Search" type="submit"/>
                </div>
              </form>
              <a href="/ofs">Home</a>
              <g:if test="${( ( request.getHeader('referer') != null ) && ( request.getHeader('referer').toLowerCase().contains('/ofs')) )}">
                > <a href="${request.getHeader('referer')}">Search Results</a>
              </g:if>
            </div>
          </div>
        </div>
      </div>

      <g:layoutBody />

      <div class="footerlinks yui3-u">
        &nbsp;<br/>
        &nbsp;<br/>
        &nbsp;<br/>
        &nbsp;<br/>
        &nbsp;<br/>
        <a href="http://partners.openfamilyservices.org.uk/?page_id=9">About</a>
        <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Local Authorities</a>
        <a href="http://partners.openfamilyservices.org.uk/?page_id=9">Channel Partners</a> <br/>

        <div id="google_translate_element"></div><script>
        function googleTranslateElementInit() {
          new google.translate.TranslateElement({
            pageLanguage: 'en',
            layout: google.translate.TranslateElement.InlineLayout.HORIZONTAL
          }, 'google_translate_element');
        }
        </script><script src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script>
      </div>

    </body>
</html>
