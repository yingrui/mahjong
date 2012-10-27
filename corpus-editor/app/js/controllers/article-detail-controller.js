'use strict';

function ArticleDetailCtrl($scope, $routeParams, ArticleRepository) {
  $scope.article = ArticleRepository.get({articleId: $routeParams.articleId}, function(phone) {
    // do nothing
  });

}

ArticleDetailCtrl.$inject = ['$scope', '$routeParams', 'ArticleRepository'];
