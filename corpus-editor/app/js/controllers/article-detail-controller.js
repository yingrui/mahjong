'use strict';

function ArticleDetailCtrl($scope, $routeParams, ArticleRepository) {

    $scope.mainTemplateUrl = 'partials/article-detail.html';
    $scope.sidebarTemplateUrl = 'partials/sidebar-index.html';
    $scope.article = ArticleRepository.get({articleId:$routeParams.articleId}, function (phone) {
        // do nothing
    });

}

ArticleDetailCtrl.$inject = ['$scope', '$routeParams', 'ArticleRepository'];
