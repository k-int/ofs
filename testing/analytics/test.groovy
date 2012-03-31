#!/usr/bin/groovy

// @GrabResolver(name='es', root='https://oss.sonatype.org/content/repositories/releases')
@Grapes([
  @Grab(group='org.apache.httpcomponents', module='httpmime', version='4.1.2'),
  @Grab(group='org.apache.httpcomponents', module='httpclient', version='4.0'),
  @Grab(group='org.codehaus.groovy.modules.http-builder', module='http-builder', version='0.5.0'),
  @Grab(group='org.codehaus.jackson', module='jackson-mapper-asl', version='1.9.6'),
  @Grab(group='commons-codec', module='commons-codec', version='1.6')
])



// Various resources used
// Following https://developers.google.com/accounts/docs/OAuth2ServiceAccount
// Useful info from http://www.mkyong.com/java/java-sha-hashing-example/ 
// OAuth flow here: https://developers.google.com/accounts/docs/OAuth2#scenarios
// google api's console https://code.google.com/apis/console/b/0/#access
// http://www.coderanch.com/t/134042/Security/Reading-file-keypair
// http://cunning.sharp.fm/2008/06/importing_private_keys_into_a.html
// Java client for google apis hg clone https://code.google.com/p/google-api-java-client/

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
import java.security.*;

long timenow = System.currentTimeMillis() / 1000

// Build the header
def jwt_header = ["alg":"RS256","typ":"JWT"]

// Build the required claims
def required_claims = [
  "iss":"230924794467-fst7uv7801hlnvi43fesqd91lfr7252i@developer.gserviceaccount.com",
  "scope":"https://www.googleapis.com/auth/analytics.readonly",
  "aud":"https://accounts.google.com/o/oauth2/token",
  "exp":timenow+3600, //  - Adjust for timezone
  "iat":timenow
]

// Convert maps into json
ObjectMapper mapper = new ObjectMapper();
StringWriter sw = new StringWriter();
mapper.writeValue(sw, jwt_header);
def jwt_header_string = sw.toString()

sw = new StringWriter();
mapper.writeValue(sw, required_claims);
def required_claims_string = sw.toString()

println("map: ${jwt_header_string} ${required_claims_string}");

// Encode header
String encoded_header = Base64.encodeBase64URLSafeString(jwt_header_string.getBytes());

// Encode required claims
String encoded_required_claims = Base64.encodeBase64URLSafeString(required_claims_string.getBytes());

// Generate string we will sign
String jwt_to_sign="${encoded_header}.${encoded_required_claims}"

// Load .p2 file.
println("Load p12 file (a PKCS12 cert)");
KeyStore ks = KeyStore.getInstance("PKCS12");

// In testing, point this at the key file downloaded from google
FileInputStream fis = new FileInputStream("/tmp/d8f6e2b7ceda999b6ec791cede28b7fe66cbd1e8-privatekey.p12");
ks.load(fis, "notasecret".toCharArray());

// get my private key from the kestore (.p12 file)
PrivateKey key = ks.getKey("privatekey", "notasecret".toCharArray());

// Create the signature for the header and required claims
Signature instance = Signature.getInstance("SHA256withRSA");
instance.initSign(key);
instance.update(jwt_to_sign.getBytes());
byte[] digest_result = instance.sign();

// encode signature
String signature_string = Base64.encodeBase64URLSafeString(digest_result)

// Debug - display request
println("req: ${jwt_to_sign}.${signature_string}");


// Create rest endpoint
def auth_endpoint = new RESTClient( 'https://accounts.google.com/' )

auth_endpoint.request(POST) {

  requestContentType = URLENC

  uri.path='o/oauth2/token'

  body = [ 
    'grant_type':'assertion',
    'assertion_type':'http://oauth.net/grant_type/jwt/1.0/bearer',
    'assertion':"${jwt_to_sign}.${signature_string}"
  ]


  response.success = { resp, data ->
    println("response status: ${resp.statusLine}")
    println("Response data code: ${data?.code}");
  }

  response.failure = { resp, reader ->
    println( resp )
    println( resp.statusLine )
    resp.headers.each { h ->
      println(h);
    }

    println("dump....");
    System.out << reader;
  }

}


