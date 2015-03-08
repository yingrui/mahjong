corpusEditorDirectives.directive('myWordFreqEditor', function () {

    return {
        priority: 0,
        templateUrl: '/public/partials/directives/my-word-freq-editor.htm',
        replace: false,
        restrict: 'E',
        link: function (scope, iElement, iAttrs) {
            var wordFreq = scope[iAttrs.ngModel];

        }
    };

});
