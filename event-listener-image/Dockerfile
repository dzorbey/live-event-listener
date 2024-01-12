FROM openjdk:19

EXPOSE 2700

RUN groupadd -g 1000 -r event \
 && useradd -u 1000 --no-log-init -r -g event --create-home event


USER event
WORKDIR /home/event

RUN mkdir -p app

COPY event-listener-image/run.sh app/run.sh

COPY target/live-event-listener-0.0.1-SNAPSHOT.jar app/live-event-listener.jar

CMD ["/home/event/app/run.sh"]