#!/usr/bin/groovy

@GrabResolver(name='es', root='https://oss.sonatype.org/content/repositories/releases')
// @Grab(group='com.gmongo', module='gmongo', version='0.9.2')
@Grapes([
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='com.gmongo', module='gmongo', version='0.9.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0'),
  @Grab(group='org.codehaus.jackson', module='jackson-mapper-asl', version='1.9.6'),
  @Grab(group='commons-codec', module='commons-codec', version='1.6')
])


// Following https://developers.google.com/accounts/docs/OAuth2ServiceAccount
// Useful info from http://www.mkyong.com/java/java-sha-hashing-example/ 
// OAuth flow here: https://developers.google.com/accounts/docs/OAuth2#scenarios

import org.codehaus.jackson.map.ObjectMapper
import org.apache.commons.codec.binary.Base64
import java.security.MessageDigest;
import static groovyx.net.http.ContentType.URLENC
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*
import groovyx.net.http.*
import groovy.util.slurpersupport.GPathResult
import org.apache.http.entity.mime.*
import org.apache.http.entity.mime.content.*
import java.nio.charset.Charset

// Build the assertion
def jwt_header = ["alg":"RS256","typ":"JWT"]
def required_claims = [
  "iss":"230924794467@developer.gserviceaccount.com",
  "scope":"https://www.googleapis.com/auth/analytics",
  "aud":"https://accounts.google.com/o/oauth2/token",
  "exp":1328554385,
  "iat":1328550785
]

ObjectMapper mapper = new ObjectMapper();
StringWriter sw = new StringWriter();
mapper.writeValue(sw, jwt_header);
def jwt_header_string = sw.toString()

sw = new StringWriter();
mapper.writeValue(sw, required_claims);
def required_claims_string = sw.toString()

println("map: ${jwt_header_string} ${required_claims_string}");

// We post the assertion to https://accounts.google.com/o/oauth2/token

String encoded_header = new String(Base64.encodeBase64(jwt_header_string.getBytes()));
String encoded_required_claims = new String(Base64.encodeBase64(required_claims_string.getBytes()));

String jwt_to_sign="${encoded_header}.${encoded_required_claims}"

MessageDigest md = MessageDigest.getInstance("SHA-256");
md.update(jwt_to_sign.getBytes())
byte[] digest_result = md.digest();

String signature_string = new String(Base64.encodeBase64(digest_result))

println("req: ${jwt_to_sign}.${signature_string}");

def auth_endpoint = new RESTClient( 'https://accounts.google.com/o/oauth2/token' )

auth_endpoint.request(POST) {

  requestContentType = URLENC

  // response.path = 'update.xml',
  body = [ 
    grant_type:'assertion',
    assertion_type:'http://oauth.net/grant_type/jwt/1.0/bearer',
    'assertion':"${jwt_to_sign}.${signature_string}"
  ]


  response.success = { resp, data ->
    println("response status: ${resp.statusLine}")
    println("Response data code: ${data?.code}");
  }

  response.failure = { resp ->
    println( resp.statusLine )
  }

}


