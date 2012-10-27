'use strict';

/* Controllers */

function ArticleListCtrl($scope, ArticleRepository) {
    $scope.articles = ArticleRepository.query();
    $scope.sortReverse = function() {
        var sortBy = $scope.orderProp;
        if("date" == sortBy) {
            return true;
        }
        return false;
    };
}

ArticleListCtrl.$inject = ['$scope', 'ArticleRepository'];
