#!/bin/sh

# Uncomment the next line for testing
#set -x

if [ $# -ne 1 ]; then
  echo 1>&2 Usage: $0 '<queryString>'
  echo 'E.g., ./trustDemoClient.sh "request(spanishCourse,Session)@eLearn"'
  exit 127
fi

cd ..

echo ant demoClient -DqueryString=$1
ant demoClient -DqueryString=$1