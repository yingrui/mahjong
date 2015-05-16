#! /bin/bash

if [ ! -e vectors.dat ]; then
  java -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.word2vec.Word2VecTrainingApp
fi

export M2_REPO=~/.m2/repository
java -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.word2vec.Word2VecDemo

