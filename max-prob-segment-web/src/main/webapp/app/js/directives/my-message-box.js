
corpusEditorDirectives.directive('myMessageBox', ['MessageBox',function (messageBox) {

    return {
        priority:0,
        templateUrl:'/app/partials/directives/my-message-box.htm',
        restrict:'A',
        link:function (scope, ele, iAttrs) {
            scope.hide = function() {
                ele.css("display", "none");
            };
            scope.show = function() {
                ele.css("display", "");
            }

            scope.$watch(function(){return messageBox.getMessage().id}, function(){
                scope.content = messageBox.getMessage().content;
                if(!_.isEmpty(scope.content)) {
                    scope.show();
                }
            });

            scope.hide();
        }
    };


}]);
