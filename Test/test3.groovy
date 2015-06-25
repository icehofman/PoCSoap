@Grab('com.github.groovy-wslite:groovy-wslite:1.1.2')

import groovy.xml.*
import wslite.soap.*
import wslite.http.auth.*

def client = new SOAPClient('https://dummy:11443/ClinicalDocumentWSService/ClinicalDocumentWS?wsdl')
client.authorization = new HTTPBasicAuthorization("mirth", "password")

// Grab the latest member list
def members = getSubjectGroupMemberIds(client, 'GROUP001', 'NEW').results
def memberCount = members.list.size()
println "There are ${memberCount} members initially"

// Get a patient
def retrievedSubject = getPatientsByFilterWithoutClinicalItems(client, {
    firstName("Bob")
    lastName("Bunting")
    numRows(1)
  })
def subjectId = retrievedSubject.results.list.id
println "Subject ID: ${subjectId}, name: ${retrievedSubject.results.list.name.first} ${retrievedSubject.results.list.name.middle} ${retrievedSubject.results.list.name.last}"

// Add the patient to the subjects of interest list (NEW, ACTIVE, EXCLUDED, RETIRED)
println XmlUtil.serialize( createSubjectGroupMember(client, 'GROUP001', 'NEW', 'MYSTATUS', subjectId) )

// Grab the latest member list
members = getSubjectGroupMemberIds(client, 'GROUP001', 'NEW').results
assert  members.list.size() == (memberCount + 1)

println XmlUtil.serialize(members)


// SOAP call methods

def getSubjectGroupMemberIds(client, groupId, groupStatusId){
  doSOAPRequest( client, 'getSubjectGroupMemberIds', {
    subjectGroupId( groupId )
    subjectGroupStatusId( groupStatusId )
  })
}

/**
 * Typically search by forename, surname, dob and gender
 * also by patient alias (NHS # or MRN #)
 **/
def getPatientsByFilterWithoutClinicalItems(client, subjectFilter){
  doSOAPRequest( client, 'getPatientsByFilterWithoutClinicalItems', {
    subjectFilterModel subjectFilter
  })
}


def createSubjectGroupMember(client, groupId, groupStatusId, recFacilityId, patientId){
  doSOAPRequest( client, 'createSubjectGroupMember', {
    subjectGroupId( groupId )
    subjectGroupStatusId( groupStatusId )
    subjectId( patientId )
    receivingFacilityId ( recFacilityId )
    createSubjectGroupMemberExt( 0 ) // '0' causes an update to fail silently if it exists already.
  })
}


/**
 * Do a SOAP request with the given command and payload.
 * Doesn't massage the payload, this method is just to reduce copy/paste for the SOAP request.
 **/
def doSOAPRequest(client, command, commandBody){
  def response = client.send(
                           connectTimeout:5000,
                           readTimeout:20000,
                           useCaches:false,
                           followRedirects:false,
                           sslTrustAllCerts:true) {
    envelopeAttributes "xmlns:ejb":"http://ejb.results.mirth.com/"
    body {
        "ejb:${command}"(commandBody)
    }
  }
  okStatus(response) ? response."${command}Response".'return' : null
}


/**
 * Check that the HTTP response is good.
 * Shamelessly lifted from https://confluence.sakaiproject.org/display/WEBSVCS/WS-Groovy
 **/
def okStatus( SOAPResponse response ){
  return response.httpResponse.statusMessage=="OK"
}
