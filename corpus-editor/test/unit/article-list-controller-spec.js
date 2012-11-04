'use strict';

/* jasmine specs for controllers go here */
describe('Article List Controller', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });


    beforeEach(module('corpusEditorServices'));


    describe('PhoneListCtrl', function(){
        var scope, ctrl, $httpBackend;

        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('corpus/articles.json').
                respond([{name: 'word1'}, {name: 'word2'}]);

            scope = $rootScope.$new();
            ctrl = $controller(ArticleListCtrl, {$scope: scope});
        }));


        it('should create "articles" model with 2 articles fetched from xhr', function() {
            expect(scope.articles).toEqual([]);
            $httpBackend.flush();

            expect(scope.articles).toEqualData(
                [{name: 'word1'}, {name: 'word2'}]);
        });


        it('should set the default value of orderProp model', function() {
            expect(scope.orderProp).toBe('date');
        });
    });
});
