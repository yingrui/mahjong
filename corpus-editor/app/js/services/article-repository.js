'use strict';

/* Services */

var corpusEditorServices = angular.module('corpusEditorServices', ['ngResource'])

corpusEditorServices.
    factory('ArticleRepository', function ($resource) {
        return $resource('corpus/:articleId.json', {}, {
            query:{method:'GET', params:{articleId:'articles'}, isArray:true}
        });
    });
