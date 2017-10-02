#!/bin/sh
export ASC_DIRECTORY=$(pwd)/pgp
mkdir pgp
mkdir /root/.gnupg
echo "${SONATYPE_PGP_KEYS_64}" | base64 -d > ./pgp/keys.asc
ls -als ./pgp
more ./pgp/pubring.asc
gpg --version
gpg --import ./pgp/keys.asc
