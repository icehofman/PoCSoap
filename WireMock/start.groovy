@Grab('com.github.tomakehurst:wiremock:1.56')

import com.github.tomakehurst.wiremock.*
import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig

def wireMockServer = new WireMockServer(wireMockConfig().port(8089).withRootDirectory('/home/icehofman/AGCO/PocSoap/WireMock')) //No-args constructor will start on port 8080, no HTTPS
wireMockServer.start()


// Do some stuff

//WireMock.reset()

// Finish doing stuff

//wireMockServer.stop()
