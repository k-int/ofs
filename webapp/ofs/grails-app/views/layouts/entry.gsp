<!DOCTYPE html>
<html>
    <head>
        <title><g:layoutTitle default="OFS Search Results" /></title>

        <link rel="stylesheet" href="http://yui.yahooapis.com/3.4.1/build/cssreset/reset.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.4.1/build/cssfonts/fonts.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.4.1/build/cssbase/base.css" type="text/css"/>
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.4.1/build/cssgrids/grids-min.css" type="text/css">

        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />

        <link rel="shortcut icon" href="${resource(dir:'images',file:'ofs_favicon.png')}" type="image/png" />
        <g:layoutHead />
        <g:javascript library="application" />

    </head>
    <body class="search-results yui3-skin-sam">
      <div id="pageheader">
        <div id="headerinner">
          <h1 style="display:inline;">${entry['dc.title']} via ${entry['feedback_name_s']}</h1>

          <!-- AddThis Button BEGIN -->
          <div class="addthis_toolbox addthis_default_style at_bar">
            <a class="addthis_button" style="float: left">Share:  </a>
            <a class="addthis_button_preferred_1" style="text-decoration:none;"></a>
            <a class="addthis_button_preferred_2" style="text-decoration:none;"></a>
            <a class="addthis_button_preferred_3" style="text-decoration:none;"></a>
            <a class="addthis_button_preferred_4" style="text-decoration:none;"></a>
            <a class="addthis_button_google_plusone"></a>
            <a class="addthis_counter addthis_bubble_style"></a>
            <script type="text/javascript">
              var addthis_config = {"data_track_clickback":true, "data_track_addressbar":true};
              var addthis_share = {
                templates : {
                  twitter : "Check out {{title}} on {{url}} via @OpenFamilyS"
                }
              };
            </script>
            <script type="text/javascript" src="http://s7.addthis.com/js/250/addthis_widget.js#pubid=${grailsApplication.config.ofs.addthis.code}"></script><br/>
          </div>
        </div>
      </div>

        <g:layoutBody />

    <div id="footercontainer">
      <div id="footer">
        <hr/>
        <a href="http://partners.openfamilyservices.org.uk/?page_id=9">About</a>
        <a href="http://partners.openfamilyservices.org.uk/?page_id=96">Local Authorities</a>
        <a href="http://partners.openfamilyservices.org.uk/?page_id=399">Channel Partners</a>
        <a href="http://www.openfamilyforum.org.uk/">Forum</a>
        <a href="http://www.openfamilyforum.org.uk/fis-map.html">Find your Local FIS</a>

        <div id="google_translate_element"></div>

        <script>
        function googleTranslateElementInit() {
          new google.translate.TranslateElement({
            pageLanguage: 'en',
            layout: google.translate.TranslateElement.InlineLayout.HORIZONTAL
          }, 'google_translate_element');
        }
        </script><script src="//translate.google.com/translate_a/element.js?cb=googleTranslateElementInit"></script> <br/>
      </div>
    </div>

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

  function addLink() {
    var body_element = document.getElementsByTagName('body')[0];
    var selection;
    selection = window.getSelection();
    var pagelink = "<br /><br /> Original Source Of Information: <a href='"+document.location.href+"'>"+document.location.href+"</a><br />Copied from OpenFamilyServices.org.uk"; // change this if you want
    var copytext = selection + pagelink;
    var newdiv = document.createElement('div');
    newdiv.style.position='absolute';
    newdiv.style.left='-99999px';
    body_element.appendChild(newdiv);
    newdiv.innerHTML = copytext;
    selection.selectAllChildren(newdiv);
    window.setTimeout(function() {
      body_element.removeChild(newdiv);
    },0);
  }

  function onAddThisShare(event) {
    var targetUrl = window.location.href;
    var network = event.service;
    var action = 'share';
    if (event.service == 'facebook_like') {
      // Name Facebook Like actions specifically
      action = 'like';
      network = 'facebook';
    }
    _gaq.push(['_trackSocial', network, action]);
  }

  addthis.addEventListener('addthis.menu.share', onAddThisShare);

  document.oncopy = addLink;
</script>

    </body>
</html>


