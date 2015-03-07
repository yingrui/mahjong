'use strict';

var corpusEditorDirectives = angular.module('corpusEditorDirectives', []);
var corpusEditorServices = angular.module('corpusEditorServices', []);
var corpusEditorFilters = angular.module('corpusEditorFilters', [])

angular.module('corpusEditor', ['ui','corpusEditorFilters', 'corpusEditorServices', 'corpusEditorDirectives']).
    config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/', {
            templateUrl: '/app/layout.htm',
            mainBoardTemplateUrl: '/app/partials/main-board.html',
            }).
        when('/segment', {
            templateUrl: '/app/partials/segment/main.htm',
            controller: SegmentCtrl}).
        when('/download', {
            templateUrl: '/app/partials/download/main.htm',
            controller: function(){}}).

        when('/dictionary', {
            templateUrl: '/app/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).
        when('/dictionary/:type/:index', {
            templateUrl: '/app/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).
        when('/dictionary/:type/:index/:pinyin', {
            templateUrl: '/app/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).
        when('/dictionary/:type/:index/:pinyin/:wordHead', {
            templateUrl: '/app/partials/dict/core-dict.htm',
            controller: CoreDictionaryCtrl}).

        when('/concepts', {
            templateUrl: '/app/partials/concept/main.htm',
            controller: ConceptsCtrl}).
        when('/contact', {
            templateUrl: '/app/partials/contact/main.htm',
            controller: function(){}}).
        otherwise({redirectTo:'/'});
}]);
