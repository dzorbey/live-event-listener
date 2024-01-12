#!/bin/bash
set -eo pipefail


cd /home/event

exec java \
    -jar app/live-event-listener.jar \
        --server.port=2700
