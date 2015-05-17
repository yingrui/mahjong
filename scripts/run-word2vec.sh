#! /bin/bash

ARG_SAVE_FILE="--save-file"
SAVE_MODEL="vectors.dat"

INDEX=0
arguments=("$@")

END=$(($#-1))
for i in $(eval echo "{0..$END}"); do
    ARG=${arguments[$i]}
    if [ "$ARG" = "--save-file" ]; then
        INDEX=$(($i+1))
    fi
done

SAVE_MODEL=${arguments[$INDEX]}

if [ ! -e "$SAVE_MODEL" ]; then
  java -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.word2vec.apps.Word2VecTrainingApp $@
fi

java -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.word2vec.apps.Word2VecDemo $@

