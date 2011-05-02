<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <link rel="stylesheet" href="site_files/reset.css" type="text/css"/>
    <link rel="stylesheet" href="site_files/fonts.css" type="text/css"/>
    <link rel="stylesheet" href="site_files/base.css" type="text/css"/>

    <link rel="stylesheet" type="text/css" href="http://yui.yahooapis.com/3.3.0/build/cssgrids/grids-min.css">

    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="searchresultsmain" />

    <title><g:message code="ofs.search.title"/></title>
  </head>
  <body class="search-results">
<ul>
<li>OFS End User App : ${grailsApplication.metadata['app.version']} (Build #${grailsApplication.metadata['app.buildNumber']}</li>
<li>Build Date: ${grailsApplication.metadata['app.buildDate']},</li>
<li>Build Profile: ${grailsApplication.metadata['app.buildProfile']})</li>
</ul>
  </body>
</html>

