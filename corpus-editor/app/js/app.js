'use strict';

/* App Module */

angular.module('corpusEditor', ['corpusEditorFilters', 'corpusEditorServices', 'corpusEditorDirectives']).
  config(['$routeProvider', function($routeProvider) {
  $routeProvider.
      when('/article', {templateUrl: 'partials/article-list.html',   controller: ArticleListCtrl}).
      when('/article/:articleId', {templateUrl: 'partials/article-detail.html', controller: ArticleDetailCtrl}).
      otherwise({redirectTo: '/phones'});
}]);
