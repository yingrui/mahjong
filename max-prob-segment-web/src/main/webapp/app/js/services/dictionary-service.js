'use strict';

/* Services */

corpusEditorServices.
    factory('DictionaryService', function ($http) {
        return {
            getWordHeads: function(dict, wordIndex, callback) {
                var path = '/api/dictionary/'+dict+'/pinyin/' + wordIndex;
                $http.get(path).success(callback);
            },
            getWords: function(dict, wordHead, callback) {
                var path = '/api/dictionary/'+dict+'/heads/' + wordHead;
                $http.get(path).success(callback);
            }
        };
    });
