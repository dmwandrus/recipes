#!/bin/sh

# stop currently running karaf
run/recipes-tier-1.0-SNAPSHOT/bin/stop

# clean build
mvn clean install

# icopy deployment to run directory
rm -rf run/recipes-tier-1.0-SNAPSHOT*
cp deployment/custom-karaf/target/recipes-tier-1.0-SNAPSHOT.tar.gz run/

# un tar
cd run/
tar xvf recipes-tier-1.0-SNAPSHOT.tar.gz
cd ..

# start karaf
run/recipes-tier-1.0-SNAPSHOT/bin/start

