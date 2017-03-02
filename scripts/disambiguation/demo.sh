#! /bin/bash

CORPUS_FILE=$1
MODEL_FILE=$2
DISAMBIGUATION_LABELS=/tmp/disambiguation-labels.txt

if [[ -z "$CORPUS_FILE" ]]; then
  echo "should specify input file"
  exit -1;
fi

if [[ -z "$MODEL_FILE" ]]; then
  MODEL_FILE="disambiguation.model"
fi

DIRNAME=$(dirname $0)

echo "$DIRNAME/generate-training-data.sh --train-file $CORPUS_FILE --save-file $DISAMBIGUATION_LABELS"
$DIRNAME/generate-training-data.sh --train-file $CORPUS_FILE --save-file $DISAMBIGUATION_LABELS

if [ $? != 0 ]; then
  exit -1
fi

echo "$DIRNAME/train-disambiguation-model.sh --train-file $DISAMBIGUATION_LABELS --save-file $MODEL_FILE"
$DIRNAME/train-disambiguation-model.sh --train-file $DISAMBIGUATION_LABELS --save-file $MODEL_FILE

echo "$DIRNAME/test-disambiguation-model.sh --corpus-file $CORPUS_FILE --train-file $DISAMBIGUATION_LABELS --model $MODEL_FILE"
$DIRNAME/test-disambiguation-model.sh --corpus-file $CORPUS_FILE --train-file $DISAMBIGUATION_LABELS --model $MODEL_FILE
