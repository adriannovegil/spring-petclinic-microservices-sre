#!/bin/ash
set -e

until $(curl --output /dev/null --silent --fail http://api-gateway:8080); do
  printf '.\n'
  sleep 10
done

exec artillery "$@"
