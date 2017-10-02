#!/bin/sh
set -e -x

cd github-repo-master
mvn -s ./settings.xml -Dmaven.test.skip=true clean deploy
