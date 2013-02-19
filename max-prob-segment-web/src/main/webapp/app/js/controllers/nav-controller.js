
function NavigationCtrl($scope) {
    $scope.menu = [
        {'class': 'active', 'value': 'Home', href:"#"},
        {'class': '', 'value': 'Playground', href:"#/segment"},
        {'class': '', 'value': 'Dictionary', href:"#"},
        {'class': '', 'value': 'Concepts', href:"#"},
        {'class': '', 'value': 'Downloads', href:"#"},
        {'class': '', 'value': 'Contact', href:"#"}
    ];
}

NavigationCtrl.$inject = ['$scope'];
