@Grab('com.github.groovy-wslite:groovy-wslite:1.1.2')
import wslite.soap.*

def client = new SOAPClient('http://ovc.catastro.meh.es/ovcservweb/OVCSWLocalizacionRC/OVCCallejero.asmx?WSDL')
def response = client.send(SOAPAction:'http://tempuri.org/OVCServWeb/OVCCallejero/ConsultaMunicipio') {
    body{
        Provincia(xmlns:'http://www.catastro.meh.es/', 'ALACANT')
    }
}

response.envelope.Body.Municipios.consulta_municipiero.municipiero.muni.each{
    println "--------- ${it.nm} ---------"
    println "Cartografía: ${it.carto}"
    println "Código MHAP: ${it.locat}"
    println "Código INE: ${it.loine}"
    println "----------------------------"
    println ""
}
