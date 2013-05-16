corpusEditorDirectives.directive('myRowOfWord', function ($window) {

    return {
        priority: 0,
        templateUrl: '/app/partials/directives/row-of-word.htm',
        replace: false,
        restrict: 'A',
        link: function (scope, iElement, iAttrs) {
            var wordItem = scope[iAttrs.ngModel];

            scope.setInViewMode = function () {
                wordItem.isInEditMode = false;
                scope.wordCopy = cloneWordItem(wordItem);
                $window.setTimeout(function () {
                    iElement.bind('click', function (event) {
                        wordItem.isInEditMode = true;
                        iElement.unbind('click');
                        scope.$apply();
                    });
                }, 0);

            }
            scope.setInViewMode();
        }
    };

    function cloneWordItem(origin) {
        var newVar = {
            id: origin.id,
            word: origin.word,
            type: origin.type,
            pinyinSet: _.map(origin.pinyinSet, function(pinyin){ return pinyin;})
        };
        return  newVar;
    }

});
