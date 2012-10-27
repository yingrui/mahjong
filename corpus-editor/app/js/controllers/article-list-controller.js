'use strict';

/* Controllers */

function ArticleListCtrl($scope, ArticleRepository) {
  $scope.articles = ArticleRepository.query();
  $scope.orderProp = 'date';
}

ArticleListCtrl.$inject = ['$scope', 'ArticleRepository'];
