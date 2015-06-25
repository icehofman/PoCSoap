@Grab('com.github.groovy-wslite:groovy-wslite:1.1.2')
import wslite.soap.*


def okStatus = { response->
    return response.httpResponse.statusMessage=="OK"
}


def getLoginSessionId = {usrid, pwd, serverURL->
    def client = new SOAPClient("${serverURL}/sakai-axis/SakaiLogin.jws?wsdl")
    def response = client.send(SOAPAction:'${serverURL}/sakai-axis/SakaiLogin.jws/login',connectTimeout:5000,sslTrustAllCerts:true) {
        body {
            login() {
                id(args[0])
                pw(args[1])
            }
        }
    }
    if (okStatus(response)) {
        println "Logged in "
        def sessID = response.loginResponse.toString()
    } else {
        return null;
    }
}
def logout = {sessionID, serverURL->
    def client = new SOAPClient("${serverURL}/sakai-axis/SakaiLogin.jws?wsdl")
    response = client.send(SOAPAction:'http://localhost:8080/sakai-axis/SakaiLogin.jws/logout',sslTrustAllCerts:true) {
        body {
                logout() {
                id(sessionID)
                }
        }
    }
    if (okStatus(response)) {
        println "logged out"
    }
}

def getUserNameFromSessionId = { sessID, serverURL->
        def client = new SOAPClient("${serverURL}/sakai-axis/SakaiScript.jws?wsdl")
        def response = client.send(SOAPAction:'http://localhost:8080/sakai-axis/SakaiScript.jws/getUserDisplayName',sslTrustAllCerts:true) {
            body {
                getUserDisplayName() {
                    sessionId(sessID)
                }
            }
        }
        if (okStatus(response)) {
            return response.getUserDisplayNameResponse.toString()
        } else {
            return null
        }
}
def isValidSession = {sessionId,serverURL->
    def client = new SOAPClient("${serverURL}/sakai-axis/SakaiScript.jws?wsdl")
    def response = client.send(SOAPAction:'${serverURL}/sakai-axis/SakaiLogin.jws/checkSession',connectTimeout:5000,sslTrustAllCerts:true) {
        body {
            checkSession() {
                id(sessionId)
            }
        }
    }
    return okStatus(response)
}

def serverURL = "http://localhost:8080"

if (args.size() < 2) { println "Args: [username,password]"; System.exit(0);}

try {

    def sessionID = getLoginSessionId(args[0],args[1],serverURL)

    if (sessionID) {
        String username = getUserNameFromSessionId(sessionID, serverURL)
        println "Logged in to session ${sessionID} for user ${username}"
        // now logout
        logout(sessionID, serverURL)
    } else {
        println "unable to login"
    }
} catch (SOAPFaultException sfe) {
        println sfe.message // faultcode/faultstring for 1.1 or Code/Reason for 1.2
    println sfe.text    // prints SOAP Envelope
    println sfe.httpResponse.statusCode
    println sfe.fault.detail.text()
}
