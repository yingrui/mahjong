
corpusEditorDirectives.directive('myMessageBox', ['MessageBox',function (messageBox) {
    var updateId = 0;

    function updated() {
        var hasUpdated = messageBox.getMessage().id > updateId;
        return hasUpdated ? 1 : 0;
    }

    return {
        priority:0,
        templateUrl:'/public/partials/directives/my-message-box.htm',
        restrict:'A',
        link:function (scope, ele, iAttrs) {
            scope.hide = function() {
                ele.css("display", "none");
            };
            scope.show = function() {
                ele.css("display", "");
            }

            scope.$watch(updated, function(){
                if(updateId != messageBox.getMessage().id) {
                    updateId = messageBox.getMessage().id;
                    scope.content = messageBox.getMessage().content;
                    if(!_.isEmpty(scope.content)) {
                        scope.show();
                    }
                }
            });

            scope.hide();
        }
    };


}]);
