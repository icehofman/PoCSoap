@Grab('com.github.groovy-wslite:groovy-wslite:1.1.2')

import wslite.soap.*

def client = new SOAPClient('http://www.w3schools.com/webservices/tempconvert.asmx')

def celsiusValue = 100

def response = client.send(SOAPAction:'http://www.w3schools.com/webservices/CelsiusToFahrenheit') {
    body {
        CelsiusToFahrenheit('xmlns':'http://www.w3schools.com/webservices/') {
            Celsius(celsiusValue)
        }
    }
}

println "${celsiusValue} Celsius = " + response.CelsiusToFahrenheitResponse.CelsiusToFahrenheitResult + " Fahrenheit"
