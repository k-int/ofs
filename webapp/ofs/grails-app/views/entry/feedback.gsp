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
    <h1>Feedback for ${entry['dc.title']} (Via ${entry['authority_shortcode']}</h1>

    <h3>Information Quality</h3>
    <p>We take the problem of information quality very seriously. you can use this form to tell us about problems with this record.
       We can't make general edits to the information, but we can remove the information if it's factually incorrect or out of date in accordance
       with data protection rules. Data owners (Local authorities, record owners) can manage this information themselves, and will also see any 
       messages left against this information.</p>

    <form>
      <input type="hidden" name="recid" value="${params.id}">
      <input type="hidden" name="auth" value="${params.authority}">
      <table>
        <tr><td>Your Name</td><td><input type="text" name="fbname"/></td></tr>
        <tr><td>Your Email Address</td><td><input type="text" name="fbemail"/></td></tr>
        <tr><td>Captcha</td><td><input type="text" name="captcha"/></td></tr>
        <tr><td>Feedback Category</td><td><input type="text" name="fbtype"/></td></tr>
        <tr><td>Feedback</td><td><input type="textarea" name="fbtext"/></td></tr>
      </table>
    </form>

  </body>
</html>
