'use strict';

var corpusEditorDirectives = angular.module('corpusEditorDirectives', []);
var corpusEditorServices = angular.module('corpusEditorServices', []);

angular.module('corpusEditor', ['corpusEditorFilters', 'corpusEditorServices', 'corpusEditorDirectives']).
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
        when('/contact', {
            templateUrl: '/app/partials/contact/main.htm',
            controller: function(){}}).
        otherwise({redirectTo:'/'});
}]);
