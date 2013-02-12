'use strict';

function NavigationCtrl($scope, $location, i18n) {
    $scope.nav = function(path) {
        $location.path(path);
    }
    $scope.i18n = i18n;
}

NavigationCtrl.$inject = ['$scope', '$location', 'i18n'];