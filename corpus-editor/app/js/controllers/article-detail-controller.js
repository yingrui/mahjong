'use strict';

function ArticleDetailCtrl($scope, $routeParams, ArticleRepository) {
    $scope.article = getArticleById($routeParams.articleId);
    $scope.refresh = function() {
        this.article = getArticleById($routeParams.articleId);
    }

    function getArticleById(articleId) {
        return ArticleRepository.get({articleId:articleId}, function (article) {
            // do nothing
        });
    }
}

ArticleDetailCtrl.$inject = ['$scope', '$routeParams', 'ArticleRepository'];
