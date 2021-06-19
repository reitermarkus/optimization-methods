#!/bin/bash

# repeatedly run firefly/reference algorithm and log results to outputs directory 
# used for evaluating/comparing algorithms

alg="reference" # firefly or reference

file="scripts/${alg}_dtlz.xml"
for num in {0..10}; do
  outfilename="outputs/out_${alg}_${num}.tsv"
  echo "write to file "$outfilename
  sed -i "s#file.tsv#${outfilename}#" ${file}
  ./gradlew run --args="-s ${file}"
  sed -i "s#${outfilename}#file.tsv#" ${file}
done