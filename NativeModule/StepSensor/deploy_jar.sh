#!/bin/sh -xe
MODULEDIR=`dirname $0`
MODULEDIR=`cd $MODULEDIR && pwd -P`
GRADLEDIR=${MODULEDIR}/Android
BASEDIR=${MODULEDIR}/../../

pushd ${GRADLEDIR} > /dev/null

DEPLOYDIR=${BASEDIR}/Assets/StepSensor/Plugins/Android

# archive and deploy
./gradlew clean assembleRelease

jar cvf "${DEPLOYDIR}"/StepSensor.jar -C app/build/intermediates/classes/release jp

popd
