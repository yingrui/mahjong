'use strict';

function ArticleDetailCtrl($scope, $routeParams, ArticleRepository) {
    $scope.article = getArticleById($routeParams.articleId);
    $scope.color = true;
    $scope.refresh = function() {
        this.article = getArticleById($routeParams.articleId);
    }

    $scope.enableColor = function() {
        if(this.color) {
            $(".article-detail").addClass("with-color");
        } else {
            $(".article-detail").removeClass("with-color");
        }
    }

    function getArticleById(articleId) {
        return ArticleRepository.get({articleId:articleId}, function (article) {
            // do nothing
        });
    }
}

ArticleDetailCtrl.$inject = ['$scope', '$routeParams', 'ArticleRepository'];
