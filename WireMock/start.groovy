@Grab('com.github.tomakehurst:wiremock:1.56')
@Grab('org.spockframework:spock-core:1.0-groovy-2.4')
@Grab('com.github.groovy-wslite:groovy-wslite:1.1.2')

import wslite.soap.*

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import spock.lang.*

import static com.github.tomakehurst.wiremock.client.WireMock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

class WireMocking extends Specification {
  def wireMockServer = new WireMockServer(wireMockConfig().port(8089).withRootDirectory(new File(getClass().protectionDomain.codeSource.location.path).parent))
  def wireMock = new WireMock("localhost", 8089)

  def setup() {
    wireMockServer.start()
  }

  def cleanup() {
    wireMockServer.stop()
  }

  def "Test01"() {
    when:
    String html = 'http://localhost:8089/test'.toURL().text

    then:
    println html
    assert 'WireMock!' == html

  }

  def "Test02"() {
    when:
    wireMock.register(get(urlEqualTo("/test2"))
    .willReturn(aResponse()
    .withStatus(200)
    .withBody("test02")
    ))

    then:
    String html = 'http://localhost:8089/test2'.toURL().text
    println html
    assert 'test02' == html
  }

  def "Test03"() {
    when:
    def client = new SOAPClient('http://localhost:8089/test3')
    def response = client.send() {
      envelopeAttributes "stuff:inner":"http://foo.com/"
      body{

        }
      }

    then:
    response

  }
}