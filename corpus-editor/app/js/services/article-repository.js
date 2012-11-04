'use strict';

/* Services */

angular.module('corpusEditorServices', ['ngResource']).
    factory('ArticleRepository', function ($resource) {
        return $resource('corpus/:articleId.json', {}, {
            query:{method:'GET', params:{articleId:'articles'}, isArray:true}
        });
    });
