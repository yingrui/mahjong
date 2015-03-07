'use strict';

/* Services */

corpusEditorServices.
    factory('DictionaryService', function ($http, PartOfSpeechRepository, MessageBox) {
        function extendWord(word) {
            _.each(word.partOfSpeeches, function (wordFreq) {
                wordFreq.partOfSpeech = _.find(PartOfSpeechRepository.getAll(), function (pos) {
                    return wordFreq.partOfSpeech.id == pos.id;
                });
            });
        }

        return {
            getWordHeads: function(dict, wordIndex, callback) {
                var path = '/api/dictionary/'+dict+'/pinyin/' + wordIndex;
                $http.get(path).success(callback);
            },
            getWords: function(dict, wordHead, callback) {
                var path = '/api/dictionary/'+dict+'/heads/' + wordHead;
                $http.get(path).success(function(data){
                    _.each(data, function(word){
                        extendWord(word);
                    });
                    callback(data);
                });
            },
            save: function(word, onSuccess) {
                var path = '/api/dictionary/core/words/' + word.id;
                $http.put(path, word)
                    .success(function(data){
                        onSuccess(data);
                    }).error(function(data, status){
                        MessageBox.showWarning("Error", data);
                    });
            }
        };
    });
