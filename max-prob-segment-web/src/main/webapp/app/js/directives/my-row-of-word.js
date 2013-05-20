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

            scope.saveWordName = function(wordName) {
//                console.log(wordName);
            }
            scope.saveWordType = function(wordType) {
//                console.log(wordType);
            }
            scope.saveWordPinyin = function(pinyinSet) {
//                console.log(pinyinSet);
            }
        }
    };

    function cloneWordItem(origin) {
        var newVar = {
            id: origin.id,
            word: origin.word,
            type: origin.type,
            pinyinSet: _.map(origin.pinyinSet, function(pinyin){ return pinyin;}),
            partOfSpeeches: _.map(origin.partOfSpeeches, function(wordFreq){ return wordFreq}),
            conceptSet: _.map(origin.conceptSet, function(concept){ return concept})
        };
        return  newVar;
    }

});
