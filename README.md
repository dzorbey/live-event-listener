# Java-dzorbey

This is a stream-event-client application, listening to three separate endpoints running in localhost which continuously exposes stream of events. The client is running a platform quantity of separate threads simultaneously listening to these endpoints, and the responses are exposed through a rest endpoint [GET /listener/results] in json format. An example json output is provided in the resources section.

The application is scalable in a way that, whenever there is a new eventType or platform introduced, 
it will be automatically be adjusted to the new scenario, the change required is in a single place only. 

The event listening can be replayed through another endpoint [POST /listener/restart] without the requirement of an application restart.
The restart endpoint also requires the "duration", and "event-user-limit" (userCounter) header fields for dynamic execution planning.

The endpoints are exposed through swagger documentation on;

[http://localhost:2700/swagger-ui/index.html]

The endpoints use the same authentication scheme as the event server, and the same username/password needs to be used.



#Docker build

If you are planning on running in windows environment, you might need to update the [stream.server.ip] setting 
in application.properties file with your current actual local IP and a [mvn clean install] before building the image.
Docker having issues in windows resolving into [host.docker.internal] address. 

Command to build [windows] : (from parent level) : docker build -t live-event-listener -f live-event-image/Dockerfile .

Command to run [windows] : docker run -p 2700:2700 live-event-listener

