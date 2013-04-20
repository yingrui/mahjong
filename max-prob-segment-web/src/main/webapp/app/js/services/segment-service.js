'use strict';

/* Services */

corpusEditorServices.
    factory('SegmentService', function ($http) {
        return {
            paramMap: {
                isSupportEnglish: "segment.lang.en",
                isRecognizePinyin: "recognize.pinyin",
                isSegmentMin: "minimize.word"
            },
            segment: function(data, param, callback) {
                var path = '/api/segment?';
                var scope = this;
                _.each(param, function(value, key) {
                    path += '&' + scope.paramMap[key] + '=' + value
                });
                $http.post(path, data).success(callback);
            }
        };
    });
