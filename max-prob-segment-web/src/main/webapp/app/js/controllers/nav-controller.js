
function NavigationCtrl($scope) {
    $scope.menu = [
        {'class': 'active', 'value': 'Home', href:"#"},
        {'class': '', 'value': 'Playground', href:"#/segment"},
        {'class': '', 'value': 'Dictionary', href:"#"},
        {'class': '', 'value': 'Concepts', href:"#"},
        {'class': '', 'value': 'Downloads', href:"#"},
        {'class': '', 'value': 'Contact', href:"#"}
    ];

    $scope.select = function(item) {
        _.each(this.menu, function(ele){
            if(ele.value === item.value) {
                ele.class = 'active';
            } else {
                ele.class = '';
            }
        });
    };
}

NavigationCtrl.$inject = ['$scope'];
