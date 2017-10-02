#!/usr/bin/env bash
set -e -x
cd github-repo-master
gnatsd -p 4221&
nats-streaming-server -p 4223&
mvn -Dtest=NatsStreamingToSparkTest#testNatsToKeyValueSparkConnectorWithAdditionalSubjects clean compile test
