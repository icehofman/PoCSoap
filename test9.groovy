import groovy.json.JsonSlurper

def response = 'https://xboxapi.com/json/profile/CluckingGood'.toURL().text
def slurper = new JsonSlurper()
def json = slurper.parseText(response)

println "Gamertag: " + json.Player.Gamertag
println "Tier: " + json.Player.Status.Tier
println "Online Status: " + json.Player.Status.Online_Status
println "Gamerscore: " + json.Player.Gamerscore
