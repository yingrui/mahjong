'use strict';

/* Services */

var corpusEditorServices = angular.module('corpusEditorServices', ['ngResource'])

corpusEditorServices.
    factory('DictionaryRepository', function ($resource) {
        return $resource('corpus/:wordId.json', {}, {
            query:{method:'GET', params:{wordId:'dictionary'}, isArray:true}
        });
    });
