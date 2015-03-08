'use strict';

/* Directives */

corpusEditorDirectives.directive('word', function () {
    function bindKeyPressEvent(eleInput) {
        eleInput.bind('keypress', function (event) {
            if (event.keyCode == 13) {
                eleInput.css("display", "none");
            }
        });
    }

    return {
        priority:0,
        templateUrl:'/public/partials/directives/word.htm',
        replace:false,
        restrict:'E',
        scope:false,
        link:function (scope, iElement, iAttrs) {
            var word = scope[iAttrs.ngModel];
            var eleInput = $(iElement.find('input'));

            var eleText = $(iElement.find('span'));
            eleText.addClass("word-pos-" + word.pos.toLowerCase());

            bindKeyPressEvent(eleInput);

            scope.enableOrDisableModifyInput = function () {
                var display = eleInput.css("display");
                if ("none" == display) {
                    eleInput.css("display", "");
                    eleInput.focusEnd();
                } else {
                    eleInput.css("display", "none");
                }
            }
        }
    };


});
