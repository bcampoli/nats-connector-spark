#!/bin/sh
set -e -x

git config --global user.email "$GITHUB_EMAIL"
git config --global user.name "$GITHUB_USERNAME"

cd github-repo-master
mvn -Dmaven.test.skip=true scm-publish:publish-scm
cd ..

git clone gh-pages github-docs-updated

cp -r ./github-repo-master/target/scmpublish github-docs-updated

cd github-docs-updated

ls docs | perl -e 'print "<html><body><ul>"; while(<>) { chop $_; print "<li><a href=\"./docs/$_/api\">$_</a></li>";} print "</ul></body></html>"' > index.htm
echo "Last edited the $(date +'%Y-%m-%d at %H:%M:%S')" >> index.htm
more index.htm
git add --all
git commit -a -m "Concourse update: $(date +'%Y-%m-%d at %H:%M:%S')"
