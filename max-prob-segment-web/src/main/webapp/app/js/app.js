'use strict';

/* App Module */

angular.module('corpusEditor', ['corpusEditorFilters', 'corpusEditorServices', 'corpusEditorDirectives']).
    config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/segment', {
            templateUrl: 'layout.html',
            mainTemplateUrl: 'partials/segment/main.html',
            sidebarTemplateUrl: 'partials/sidebar.html',
            controller: SegmentCtrl}).
        when('/dictionary', {
            templateUrl: 'layout.html',
            mainTemplateUrl: 'partials/dictionary/main.html',
            sidebarTemplateUrl: 'partials/sidebar.html',
            controller: DictionaryCtrl}).
        otherwise({redirectTo:'/segment'});
}]);
