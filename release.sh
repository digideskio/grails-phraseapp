#!/bin/bash

rm -rf target/release
mkdir target/release
cd target/release
git clone https://github.com/tamershahin/grails-phraseapp.git
cd grails-phraseapp
grails clean
grails compile
grails publish-plugin --stacktrace