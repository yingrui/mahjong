'use strict';

/* Services */

corpusEditorServices.
    factory('SegmentService', function ($http) {
        return {
            paramMap: {
                isSupportEnglish: "segment.lang.en",
                isEnglishStemming: "segment.lang.en.stemming",
                isRecognizePinyin: "recognize.pinyin",
                isSegmentMin: "minimize.word"
            },
            segment: function(data, param, callback) {
                var path = '/api/segment?';
                var scope = this;
                _.each(param, function(value, key) {
                    if(value !== undefined && value !== null){
                        path += '&' + scope.paramMap[key] + '=' + value
                    }
                });
                return $http.post(path, data).success(callback);
            }
        };
    });
