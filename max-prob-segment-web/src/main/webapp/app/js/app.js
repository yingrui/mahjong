'use strict';

/* App Module */

var corpusEditorDirectives = angular.module('corpusEditorDirectives', []);
var corpusEditorServices = angular.module('corpusEditorServices', []);

angular.module('corpusEditor', ['corpusEditorFilters', 'corpusEditorServices', 'corpusEditorDirectives']).
    config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/segment', {
            templateUrl: 'layout.htm',
            mainTemplateUrl: 'partials/segment/main.htm',
            sidebarTemplateUrl: 'partials/sidebar.htm',
            controller: SegmentCtrl}).
        otherwise({redirectTo:'/segment'});
}]);
