'use strict';

/* Controllers */

function DictionaryCtrl($scope, dictionaryRepository) {
    $scope.dictionary = dictionaryRepository.query();
}

DictionaryCtrl.$inject = ['$scope', 'DictionaryRepository'];
