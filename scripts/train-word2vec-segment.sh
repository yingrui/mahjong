#! /bin/bash

java -Xmx8192m -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.word2vec.apps.MNNSegmentTrainingApp $@

java -Xmx8192m -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.word2vec.apps.MNNSegmentTest $@

