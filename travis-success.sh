#!/usr/bin/env bash

# Only deploy tags
if [ -z "$TRAVIS_TAG" ]; then
	echo "Not a tagged release, not deploying..."
	exit 0
fi

echo "Tagged release, deploying..."
openssl aes-256-cbc -K $encrypted_d70ee0d3a918_key -iv $encrypted_d70ee0d3a918_iv -in codesigning.asc.enc -out codesigning.asc -d
gpg --fast-import codesigning.asc
gradle -Psign uploadArchives