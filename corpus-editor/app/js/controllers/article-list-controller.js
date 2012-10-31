'use strict';

/* Controllers */

function ArticleListCtrl($scope, ArticleRepository, $route) {
    $scope.orderProp = 'date';
    $scope.mainTemplateUrl = 'partials/article-list.html';
    $scope.sidebarTemplateUrl = 'partials/sidebar-index.html';
    $scope.articles = ArticleRepository.query();
    $scope.sortReverse = function() {
        var sortBy = $scope.orderProp;
        if("date" == sortBy) {
            return true;
        }
        return false;
    };
}

ArticleListCtrl.$inject = ['$scope', 'ArticleRepository', '$route'];
