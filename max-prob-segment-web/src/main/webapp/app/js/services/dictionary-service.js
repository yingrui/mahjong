'use strict';

/* Services */

corpusEditorServices.
    factory('DictionaryService', function ($http, PartOfSpeechRepository) {
        return {
            getWordHeads: function(dict, wordIndex, callback) {
                var path = '/api/dictionary/'+dict+'/pinyin/' + wordIndex;
                $http.get(path).success(callback);
            },
            getWords: function(dict, wordHead, callback) {
                var path = '/api/dictionary/'+dict+'/heads/' + wordHead;
                $http.get(path).success(function(data){
                    _.each(data, function(word){
                        _.each(word.partOfSpeeches, function(wordFreq){
                            wordFreq.partOfSpeech = _.find(PartOfSpeechRepository.getAll(), function(pos) {
                                return wordFreq.partOfSpeech.id == pos.id;
                            });
                        });
                    });
                    callback(data);
                });
            },
            save: function(word) {
                var path = '/api/dictionary/words/' + word.id;
                $http.put(path, word).success()
            }
        };
    });
