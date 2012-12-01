'use strict';

/* Directives */
corpusEditorDirectives.directive('myButton',
    function () {
        return {
            restrict: 'E',
            template: '<input type="button" style="border: outset 0px; height: 30px;cursor: pointer; margin: 2px;">',
            terminal: true,
            link: function (scope, iElement, iAttrs) {
                var eleInput = $(iElement.find('input'));
                $(eleInput).attr('value', iAttrs.value)
            }
        };
    }
);
