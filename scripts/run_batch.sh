#!/bin/bash

set -euo pipefail

for config_file in configs/generated/*; do
  ./gradlew run --args="-s '${config_file}'"
done
