
corpusEditorDirectives.directive('myWordItem', function () {
    var selected = "selected";
    function bindClickEvent(ele) {
        ele.click(function (event) {
            removeClassFromSiblings(ele)
            $(ele).addClass(selected);
        });
    }

    function removeClassFromSiblings(ele) {
        _.each($(ele).parent().children(), function(li) {
            $(li).removeClass(selected);
        })
    }

    return {
        priority:0,
        templateUrl:'/app/partials/directives/word-item.htm',
        restrict:'A',
        scope:true,
        link:function (scope, iElement, iAttrs) {
            bindClickEvent(iElement);
        }
    };


});
