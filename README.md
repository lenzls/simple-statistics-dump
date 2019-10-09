# simple-statistics-dump
Statistics gathering server made for Ludum dare games

Because it already happened twice, that one and a half hours before the deadline of the Jam we decided to set up 
a simple web server, where we can send statistics about player decisions to. And both times I failed because 
of stupid reasons. 

Next time we are prepared and can reuse this!

# Running

Needs Java 11.

    java -Dserver.port=5000 -DstatisticsFileFolder=/tmp/ -jar simple-statistics-dump-0.0.1-SNAPSHOT.jar
    
`statisticsFileFolder` should end with a trailing slash! The default is `/tmp`.

# Functionality

Server receives statistics from clients, stores them all in a big json file and 
optionally returns a processed version of the total statistics that the clients 
then can show the players.

1. Clients submit their statistics to the server
2. Server appends those to the `<statisticsFileFolder>/statistics.json`
3. Server optionally processes the whole statistics file and computes some result json
4. Server responds this result to client
5. Client shows info to players

When run, the application exposes two endpoints for sending information to the server.

namely `POST <host>/submit-statistics` with body
```
{
    "ending": "ending1",
    "distanceTraveled": 2507,
    "endbossDefeated": true
}
```
    
and `GET <host>/submit-statistics?ending=ending1&distanceTraveled=2507&endbossDefeated=true`.

The POST endpoint should be preferred, but if unforeseen problems with CORS or something arise, 
then this might be a useful workaround.

# Example client code

```javascript
const SERVER_URL = "<HOST>:5000"

export async function sendStatistics(decision) {
    const content = {
        ending: decision,
        distanceTraveled: 2507,
        endbossDefeated: true
    }

    const response = await fetch(`${SERVER_URL}/submit-statistics`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(content)
    })
    console.log(await response.json());
}
```
