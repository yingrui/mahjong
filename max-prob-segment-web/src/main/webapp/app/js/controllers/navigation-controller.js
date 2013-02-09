'use strict';

function NavigationCtrl($scope, $location) {
    $scope.nav = function(path) {
        $location.path(path);
    }
}

NavigationCtrl.$inject = ['$scope', '$location'];