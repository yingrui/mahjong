#! /bin/bash

ARG_TRAIN_FILE="--train-file"
TRAIN_FILE=""

INDEX=-2
arguments=("$@")

END=$(($#-1))
for i in $(eval echo "{0..$END}"); do
    ARG=${arguments[$i]}
    if [ "$ARG" = "--train-file" ]; then
        INDEX=$(($i+1))
        TRAIN_FILE=${arguments[$INDEX]}
    fi
done

if [ ! "" = "$TRAIN_FILE" ]; then
  java -cp lib-segment-apps/target/lib-segment-1.0-rc1-jar-with-dependencies.jar me.yingrui.segment.word2vec.apps.Word2VecTrainingApp $@
fi

java -cp lib-segment-apps/target/lib-segment-1.0-rc1-jar-with-dependencies.jar me.yingrui.segment.word2vec.apps.Word2VecDemo $@

