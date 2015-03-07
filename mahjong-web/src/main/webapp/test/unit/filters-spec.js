'use strict';

describe('Filters', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(module('corpusEditorFilters'));

    describe('show username filter', function(){
        var filter;

        beforeEach(inject(function($filter) {
            filter = $filter('showUsername');
        }));

        it('should return last name and first name as user name', function(){
            expect(filter({firstName:'firstName', lastName:'lastName'})).toBe('lastName firstName');
        });

    });

    describe('show pinyin filter', function(){
        var filter;

        beforeEach(inject(function($filter) {
            filter = $filter('showPinyin');
        }));

        it('should return pinyin array separated by vertical line', function(){
            expect(filter(['ai','a'])).toBe('ai | a');
        });

    });

    describe('show part of speech filter', function(){
        var filter;

        beforeEach(inject(function($filter) {
            filter = $filter('showPartOfSpeech');
        }));

        it('should return partOfSpeeches separated by vertical line', function(){
            var array = [
                {partOfSpeech: {id:1, name:'N', note:'名词'}, freq: 10},
                {partOfSpeech: {id:2, name:'T', note:'时间词'}, freq: 12}
            ];
            expect(filter(array)).toBe('名词(N, 10) | 时间词(T, 12)');
        });

    });

    describe('show concept filter', function(){
        var filter;

        beforeEach(inject(function($filter) {
            filter = $filter('showConcept');
        }));

        it('should return concepts separated by vertical line', function(){
            var array = [
                {partOfSpeech: {id:1, name:'N', note:'名词'}, name: 'n-info', note:'信息'},
                {partOfSpeech: {id:2, name:'T', note:'时间词'}, name: 'n-abstract', note:'抽象'}
            ];
            expect(filter(array)).toBe('信息(n-info, 名词) | 抽象(n-abstract, 时间词)');
        });

    });
});
