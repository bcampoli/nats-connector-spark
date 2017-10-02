#!/usr/bin/env bash
set -e -x
cd github-repo-master
gnatsd -p 4221&
nats-streaming-server -p 4223&

for i in $(seq 0 100);
  do
    mvn -Dtest=NatsStreamingToSparkTest#testNatsToKeyValueSparkConnectorWithAdditionalSubjects test &
done

mvn -Dtest=NatsStreamingToSparkTest#testNatsToKeyValueSparkConnectorWithAdditionalSubjects test 
