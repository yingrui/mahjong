
corpusEditorDirectives.directive('myWordIndex', function () {
    var selected = "selected";

    return {
        priority:0,
        restrict:'A',
        require:'ngModel',
        link:function (scope, ele, iAttrs) {
            var elementValue = scope[iAttrs.ngModel];
            var selectedValue = scope[iAttrs.myWordIndex];

            if(elementValue === selectedValue) {
                $(ele).addClass(selected);
            }

            bindClickEvent(ele);

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
        }
    };


});
