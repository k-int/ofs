<html>
  <head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!-- Include the main layout from the grails-app/views/layouts dir - Thats where are the styles etc are imported -->
    <meta name="layout" content="main" />
    <title>OFS Search</title>
  </head>
  <body>
    <div id="main">
      <div id="wrap">
        <div id="SearchPanel">
          <g:form controller="search" action="search" method="get">
            <table>
              <tr>
                <td>
                  Search for: <input type="text" name="q"/> <input type="submit"/>
                </td>
              </tr>
            </table>
          </g:form>
        </div>
      </div>
    </div><!-- main -->
  </body>
</html>
