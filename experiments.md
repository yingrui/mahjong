## Experimental Development
**CRF** is the state-of-the-art algorithm for serial label classify problem, it has been widely adopted to apply to NLP tasks.

**Mahjong** also implemented CRF algorithm and trying to balance the between different algorithms.

**Deep Learning** is also become the hottest topic in machine learning. Our project also implements **Word2Vec**, it is going to integrate with max probability algorithm as well.

## Test Deep Learning Segment
1. Generate Chinese characters for character vector training: words.txt
```sh
./scripts/generate-word2vec-training-data.sh
Or
Run app PFRCorpusToWord2VecTrainingData in IDE, it will generate file words.txt
```

2. Train hierarchy softmax Word2Vec model for Chinese character vector
```sh
./scripts/run-word2vec.sh --save-file vectors.cn.test.dat --train-file words.txt -hs
Or
Run app Word2VecTrainingApp in IDE, it will build word2vec model vectors.dat
```

3. Test Word2Vec model: the same command without parameter --train-file
```sh
./scripts/run-word2vec.sh --save-file vectors.cn.test.dat
Or
Run app Word2VecDemo in IDE
```

4. Generate Chinese segment training data: training.txt
```sh
./scripts/generate-training-data.sh
Or
Run app PFRCorpusToSegmentSerialLabel in IDE, it will generate file training.txt
```

5. Train Chinese segment model with Multiple Layer Perceptron
```sh
./scripts/train-word2vec-segment.sh --train-file training.txt --save-file segment-vector.dat --word2vec-model vectors.cn.test.dat
Or 
Run app MNNSegmentTrainingApp in IDE
```
6. Test Chinese segment model with RNN
```sh
./scripts/test-word2vec-segment.sh --train-file training.txt --save-file segment-vector.dat --word2vec-model vectors.cn.test.dat
Or
Run app RNNSegmentTrainingApp in IDE
```
## Test CRF Segment
1. Generate Chinese segment training data: training.txt

    ./scripts/generate-training-data.sh

2. Train CRF Chinese segment model

    ./scripts/train-crf-segment.sh --train-file training.txt --save-file segment-crf.m

3. Test CRF Chinese segment model

    ./scripts/test-crf-segment.sh --train-file training.txt --save-file segment-crf.m
