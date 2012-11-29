'use strict';

function NavigationCtrl($scope, $location) {
    console.log("NavigationCtrl: ", $location.path());

    $scope.nav = function(path) {
        $location.path(path);
    }
}

NavigationCtrl.$inject = ['$scope', '$location'];