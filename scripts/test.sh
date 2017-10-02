#!/bin/sh
set -e -x
cd github-repo-master
gnatsd -p 4221&
nats-streaming-server -p 4223&
mvn clean compile test
