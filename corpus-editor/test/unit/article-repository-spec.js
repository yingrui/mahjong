'use strict';

describe('Article Repository Service', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(module('corpusEditorServices'));

    describe('ArticleRepository', function(){
        var $httpBackend, repository, routeParams;

        beforeEach(inject(function(_$httpBackend_, $injector, $routeParams) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('corpus/article1.json').
                respond({segmentResult:[[{name: 'word1'}]]});
            routeParams = $routeParams;
            repository = $injector.get('ArticleRepository');
        }));

        it('should return "article" with 1 word fetched via article repository', function() {
            var response = repository.get({articleId:'article1'});
            $httpBackend.flush();
            expect(response).toEqualData({segmentResult:[[{name: 'word1'}]]});
        });
    });
});
