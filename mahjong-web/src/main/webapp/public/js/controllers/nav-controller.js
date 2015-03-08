
function NavigationCtrl($scope, $location, userService) {
    $scope.menu = [
        {'class': 'active', 'value': 'Home', href:"#/"},
        {'class': '', 'value': 'Playground', href:"#/segment"},
        {'class': '', 'value': 'Dictionary', href:"#/dictionary"},
        {'class': '', 'value': 'Concepts', href:"#/concepts"},
        {'class': '', 'value': 'Downloads', href:"#/download"},
        {'class': '', 'value': 'Contact', href:"#/contact"}
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

    $scope.currentUser = null;

    userService.getCurrentUser(function(user) {
        $scope.currentUser = user;
    });

    $scope.hasSignedIn = function() {
        return $scope.currentUser != null;
    };

    $scope.location = function() {
        return $location.path();
    }

    $scope.$watch('location()', function(){
        checkCurrentLocation();
    })

    checkCurrentLocation();
    function checkCurrentLocation() {
        var path = $location.path();
        _.each($scope.menu, function(item){
            var href = item.href.substring(1);
            item.class = href === path ? 'active' : ''
        });
    }
}

NavigationCtrl.$inject = ['$scope', '$location', 'UserService'];
