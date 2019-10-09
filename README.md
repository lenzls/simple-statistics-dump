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
