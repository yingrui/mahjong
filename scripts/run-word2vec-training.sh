#!/bin/bash

java -cp lib-segment/target/lib-segment-1.0-SNAPSHOT.jar:$SCALA_HOME/lib/scala-library.jar me.yingrui.segment.word2vec.Word2VecTrainingApp
