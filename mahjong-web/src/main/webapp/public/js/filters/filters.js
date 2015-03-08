'use strict';

corpusEditorFilters.filter('showUsername', function () {
    return function (user) {
        return user.lastName + ' ' + user.firstName;
    };
});

corpusEditorFilters.filter('showPinyin', function () {
    return function (array) {
        return array.join(' | ');
    };
});

corpusEditorFilters.filter('showPartOfSpeech', function () {
    return function (array) {
        return _.map(array, function (wordFreq) {
            return wordFreq.partOfSpeech.note + '(' + wordFreq.partOfSpeech.name + ', ' + wordFreq.freq + ')'
        }).join(' | ');
    };
});

corpusEditorFilters.filter('showConcept', function () {
    return function (array) {
        return _.map(array, function (concept) {
            return concept.note + '(' + concept.name + ', ' + concept.partOfSpeech.note + ')'
        }).join(' | ');
    };
});