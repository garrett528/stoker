# Stoker - Kafka High Level Monitoring UI
See high level information about your Kafka cluster, Schema Registry, and Kafka Connect in one place. 
This application extends the work done on [Obsidian Dynamic's Kafdrop](https://github.com/obsidiandynamics/kafdrop). 
The backend is based off of the Kafdrop v3.24. The frontend is a remodeled Vue.js application using a similar style to
the original Kafdrop UI. This application adds the capability to see and perform basic administrator functions on Kafka Connect
and Schema Registry.

(Docker image coming. Will link to full image and provide instructions on how to use once complete).

## Requirements
* Java 11 or newer
* Kafka (version 0.11.0 or newer)
* NPM / Vue.js

Optional, additional integration:

* Schema Registry
* Kafka Connect

## Future additions
* Topic administration
* Add / delete Kafka Connectors
* Add / delete Schema Registry
* RBAC mechanism for admin functions

## Contributing
Contributions and suggestions are welcome.