'use strict';

/* Services */

corpusEditorServices.
    factory('SegmentService', function ($http) {
        return {
            segment: function(data, callback) {
                $http.post('/api/segment?recognize.pinyin=true', data).success(callback);
            }
        };
    });
