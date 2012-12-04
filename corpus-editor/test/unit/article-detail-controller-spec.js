'use strict';

describe('Article Detail Controller', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(module('corpusEditorServices'));

    describe('ArticleDetailCtrl', function(){
        var scope, ctrl, $httpBackend;

        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, $routeParams) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('corpus/article1.json').
                respond({segmentResult:[[{name: 'word1'}]]});
            $routeParams.articleId = "article1"
            scope = $rootScope.$new();
            ctrl = $controller(ArticleDetailCtrl, {$scope: scope});
        }));


        it('should create "article" model with 1 sentence fetched from xhr', function() {
            expect(scope.article.segmentResult).toEqual(undefined);
            $httpBackend.flush();

            expect(scope.article).toEqualData(  {segmentResult:[[{name: 'word1'}]]});
        });


        it('should set true as default color displaying', function() {
            expect(scope.color).toEqual(true);
        });
    });
});
