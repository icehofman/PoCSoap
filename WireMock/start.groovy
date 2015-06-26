@Grab('com.github.tomakehurst:wiremock:1.56')

import com.github.tomakehurst.wiremock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

def wireMockServer = new WireMockServer(wireMockConfig().port(8089).withRootDirectory(new File(getClass().protectionDomain.codeSource.location.path).parent))
wireMockServer.start()

//WireMock.reset()

//wireMockServer.stop()
