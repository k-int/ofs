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
    <h1>OFS Record Feedback - ${entry['dc.title']} (Via <g:message code="cv.authority_shortcode.${params.authority}"/>)</h1>

    <h3>Information Quality</h3>
    <p>We take the problem of information quality very seriously. you can use this form to tell us about problems with this record.
       We can't make general edits to the information, but we can remove the information if it's factually incorrect or out of date in accordance
       with data protection rules. Data owners (Local authorities, record owners) can manage this information themselves, and will also see any 
       messages left against this information.</p>

    <form action="/ofs/directory/${params.authority}/${params.id}/feedback" method="POST">
      <g:hiddenField name="recid" value="${params.id}"/>
      <g:hiddenField name="auth" value="${params.authority}"/>
      <g:hiddenField name="recname" value="${entry['dc.title']}"/>
      <g:hiddenField name="validation_stamp" value="${validation_stamp}"/>
      <table>
        <tr><td>Record Source</td><td><g:message code="cv.authority_shortcode.${params.authority}"/></td></td>
        <tr><td>URL</td><td><a href="/ofs/directory/${params.authority}/${params.id}">/ofs/directory/${params.authority}/${params.id}</a></td></td>
        <tr><td>Title</td><td>${entry['dc.title']}</td></td>

        <tr><td><label for="fbname">Your Name*</label></td><td><input type="text" name="fbname"/></td></tr>
        <tr><td><label for="fbmail">Your Email Address*</label></td><td><input type="text" name="fbemail"/></td></tr>
        <tr><td><label for="fbmail">Your IP Address*</label></td><td>${remote_addr}</td></tr>
        <tr><td><label for="fbtype">Feedback Category*</label></td>
            <td><select name="fbtype"/>
                   <option value="remove">Critical - Information is incorrect and should be removed (Please give details below)</option>
                   <option value="recfb">Record Feedback - Some information to help improve the record (Please give details below)</option>
                   <option value="srchfb">Search Feedback - I didn't expect to find this record in my search (Please give details below)</option>
                </select>
            </td></tr>

        <tr><td><label for="fbtext">Feedback*</label></td><td><g:textArea name="fbtext" style='width: 500px; height: 200px;'/></td></tr>
        <tr><td><label for="fbcaptchaResponse">Captcha*<br/>Please confirm you are a human being</label></td><td>
            <jcaptcha:jpeg name="image" height="75" width="200" /><br/>
            <g:textField name="fbcaptchaResponse" value="" />
            <!-- jcaptcha:wav name="<captchaname>" autostart="0" --></td></tr>
        <tr><td colspan="2" style="text-align:centre;"><input type="submit"/></td></tr>
      </table>
    </form>

  </body>
</html>
