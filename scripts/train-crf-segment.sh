#! /bin/bash

java -Xmx8192m -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.crf.app.CRFSegmentTrainingApp $@

java -Xmx8192m -cp lib-segment/target/lib-segment-1.0-SNAPSHOT-jar-with-dependencies.jar me.yingrui.segment.crf.app.CRFSegmentTestApp $@

