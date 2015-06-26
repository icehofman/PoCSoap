@Grab('com.github.tomakehurst:wiremock:1.56')
@Grab('org.spockframework:spock-core:1.0-groovy-2.4')

import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock
import spock.lang.Specification

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
    wireMock.register(get(urlEqualTo("/test2"))
    .willReturn(aResponse()
    .withStatus(200)
    .withBody("test02")
    ))

    then:
    println wireMock
  }
}
