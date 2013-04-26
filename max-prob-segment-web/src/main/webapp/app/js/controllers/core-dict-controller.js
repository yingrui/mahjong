
function CoreDictionaryCtrl($scope, dictionaryService, i18n) {

    $scope.wordIndexes = ["A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"];
    $scope.wordHeads = [];
    $scope.wordItems = [];

    $scope.switchIndex = function(wordIndex) {
        dictionaryService.getWordHeads('core', wordIndex, function(result) {
            $scope.wordHeads = result;
            getWords(result[0]);
        });
    };

    $scope.switchHead = function(wordHead) {
        getWords(wordHead);
    };

    function getWords(wordHead) {
        dictionaryService.getWords('core', wordHead, function(result) {
            $scope.wordItems = result;
        });
    };
}

CoreDictionaryCtrl.$inject = ['$scope', 'DictionaryService'];
