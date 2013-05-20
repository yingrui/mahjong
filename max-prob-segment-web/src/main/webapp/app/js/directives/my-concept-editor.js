corpusEditorDirectives.directive('myConceptEditor', function () {

    return {
        priority: 0,
        templateUrl: '/app/partials/directives/my-concept-editor.htm',
        replace: false,
        restrict: 'E',
        link: function (scope, iElement, iAttrs) {
            var wordFreq = scope[iAttrs.ngModel];

        }
    };

});
