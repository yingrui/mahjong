'use strict';

/* App Module */

angular.module('corpusEditor', ['corpusEditorFilters', 'corpusEditorServices', 'corpusEditorDirectives']).
    config(['$routeProvider', function ($routeProvider) {
    $routeProvider.
        when('/article', {
            templateUrl: 'layout.html',
            mainTemplateUrl: 'partials/article-list.html',
            sidebarTemplateUrl: 'partials/sidebar-index.html',
            controller:ArticleListCtrl}).
        when('/article/:articleId', {
            templateUrl: 'layout.html',
            mainTemplateUrl: 'partials/article-detail.html',
            sidebarTemplateUrl: 'partials/sidebar-index.html',
            controller:ArticleDetailCtrl}).
        otherwise({redirectTo:'/phones'});
}]);
