corpusEditorDirectives.directive('myRowOfWord', ['$window', 'DictionaryService', 'PartOfSpeechRepository', 'ConceptRepository', function ($window, dictionaryService, partOfSpeechRepository, conceptRepository) {

    return {
        priority:0,
        templateUrl:'/app/partials/directives/row-of-word.htm',
        replace:false,
        restrict:'A',
        link:function ($scope, iElement, iAttrs) {
            var wordItem = $scope[iAttrs.ngModel];

            function RowOfWord() {
                this.setInViewMode = function () {
                    $scope.isInEditMode = false;
                    this.wordCopy = cloneWordItem(wordItem);
                };
                this.saveWord = function () {
                    var rowOfWord = this;
                    dictionaryService.save(wordItem, function (data) {
                        rowOfWord.setInViewMode();
                    });
                };
                this.setInEditMode = function () {
                    $scope.isInEditMode = true;
                };
                this.addPinyin = function () {
                    wordItem.pinyinSet.push('');
                };
                this.deletePinyin = function (pinyin) {
                    wordItem.pinyinSet = _.without(wordItem.pinyinSet, pinyin);
                };
                this.addPartOfSpeech = function () {
                    var wordFreq = {freq:1, partOfSpeech:partOfSpeechRepository.getUnknownPos()};
                    wordItem.partOfSpeeches.push(wordFreq);
                };
                this.deletePartOfSpeech = function(partOfSpeech) {
                    wordItem.partOfSpeeches = _.filter(wordItem.partOfSpeeches, function(wordFreq){
                        return wordFreq.partOfSpeech.id != partOfSpeech.id;
                    });
                };
                this.addConcept = function() {
                    var wordFreq = _.find(wordItem.partOfSpeeches, function(wordFreq) {
                        return wordFreq.partOfSpeech.name.charAt(0) == 'N' ||
                            wordFreq.partOfSpeech.name.charAt(0) == 'A' ||
                            wordFreq.partOfSpeech.name.charAt(0) == 'V'
                    });
                    if(wordFreq != null && wordFreq != undefined) {
                        if(wordFreq.partOfSpeech.name.charAt(0) == 'N') {
                            wordItem.conceptSet.push(conceptRepository.getByName('n-noun'));
                        }
                        if(wordFreq.partOfSpeech.name.charAt(0) == 'V') {
                            wordItem.conceptSet.push(conceptRepository.getByName('v-verb'));
                        }
                        if(wordFreq.partOfSpeech.name.charAt(0) == 'A') {
                            wordItem.conceptSet.push(conceptRepository.getByName('a-adj'));
                        }
                    }
                }
            }

            function cloneWordItem(origin) {
                var newVar = {
                    id:origin.id,
                    word:origin.word,
                    type:origin.type,
                    pinyinSet:_.map(origin.pinyinSet, function (pinyin) {
                        return pinyin;
                    }),
                    partOfSpeeches:_.map(origin.partOfSpeeches, function (wordFreq) {
                        return wordFreq
                    }),
                    conceptSet:_.map(origin.conceptSet, function (concept) {
                        return concept
                    })
                };
                return  newVar;
            }

            var rowOfWord = new RowOfWord();
            $.extend($scope, rowOfWord);
            $scope.setInViewMode();
        }
    };
}]);
