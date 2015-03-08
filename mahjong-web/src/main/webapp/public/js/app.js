'use strict';

var corpusEditorDirectives = angular.module('corpusEditorDirectives', []);
var corpusEditorServices = angular.module('corpusEditorServices', []);
var corpusEditorFilters = angular.module('corpusEditorFilters', [])

angular.module('corpusEditor', ['ui','corpusEditorFilters', 'corpusEditorServices', 'corpusEditorDirectives']).
    config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/', {
            templateUrl: '/public/layout.htm',
            mainBoardTemplateUrl: '/public/partials/main-board.html',
            }).
        when('/segment', {
            templateUrl: '/public/partials/segment/main.htm',
            controller: SegmentCtrl}).
        when('/download', {
            templateUrl: '/public/partials/download/main.htm',
            controller: function(){}}).

        when('/dictionary', {
            templateUrl: '/public/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).
        when('/dictionary/:type/:index', {
            templateUrl: '/public/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).
        when('/dictionary/:type/:index/:pinyin', {
            templateUrl: '/public/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).
        when('/dictionary/:type/:index/:pinyin/:wordHead', {
            templateUrl: '/public/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).

        when('/concepts', {
            templateUrl: '/public/partials/concept/main.htm',
            controller: ConceptsCtrl}).
        when('/contact', {
            templateUrl: '/public/partials/contact/main.htm',
            controller: function(){}}).
        otherwise({redirectTo:'/'});
}]);
