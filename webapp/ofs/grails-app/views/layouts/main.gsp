<!DOCTYPE html>
<html>
    <head>

    <meta property="dc.title" name="dc.title" content="Search the UK Directory of Child care, registered Childminders and Family Information"/>

    <meta property="title" name="title" content="Open Family Services - The UK National Directory of Child care, registered Childminders, Family Information, Positive activities and Family Services"/>
    <meta property="og:title" name="og:title" content="Open Family Services - The UK National Directory of Child care, registered Childminders, Family Information, Positive activities and Family Services"/>

    <meta property="dc.description" name="dc:description" name="dc.description" content="Find information on a wide range of Family Services, Activities and OfSTED registered Childcare from Family Information Service’s across England."/>

    <meta property="description" name="description" name="description" content="Find information on a wide range of Family Services, Activities and OfSTED registered Childcare from Family Information Service’s across England."/>

    <meta property="og:description" name="og:description" content="Find information on a wide range of Family Services, Activities and OfSTED registered Childcare from Family Information Service’s across England."/>

    <meta property="keywords" name="keywords" content="Family, Childcare, Search, Information, Positive Activities, Events, Organisations, child care, babysitters, childminders, registered childminders"/>

        <title><g:layoutTitle default="Search the UK Directory of Child care, registered Childminders and Family Information" /></title>

        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssreset/reset.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssfonts/fonts.css" type="text/css"/> 
        <link rel="stylesheet" href="http://yui.yahooapis.com/3.3.0/build/cssbase/base.css" type="text/css"/>

        <link rel="stylesheet" href="${resource(dir:'css',file:'main.css')}" />

        <link rel="shortcut icon" href="${resource(dir:'images',file:'ofs_favicon.png')}" type="image/png" />


        <g:layoutHead />
        <g:javascript library="application" />

    </head>
    <body class="yui3-skin-sam  yui-skin-sam">

        <g:layoutBody />

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

  document.oncopy = addLink;

</script>

    </body>
</html>
