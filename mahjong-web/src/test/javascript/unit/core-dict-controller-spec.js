'use strict';

describe('Core Dictionary Controller', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(module('corpusEditorServices'));

    describe('CoreDictionaryCtrl', function(){
        var scope, ctrl, $httpBackend;

        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, DictionaryService, PartOfSpeechRepository) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectGET('/concept').
                respond([]);
            $httpBackend.expectGET('/dictionary/core/pinyin/a').
                respond(["啊","擀","炝","阿"]);
            // TODO: remove this redundant call.
            $httpBackend.expectGET('/concept').
                respond([]);
            $httpBackend.expectGET('/dictionary/core/heads/啊').
                respond([{"word":"啊哈"},{"word":"啊"},{"word":"啊哟"},{"word":"啊呀"}]);

            scope = $rootScope.$new();
            ctrl = $controller(CoreDictionaryCtrl, {"$scope": scope, "dictionaryService": DictionaryService, "partOfSpeechRepository": PartOfSpeechRepository, "$routeParams": {type:"core",index:"a",pinyin:"a",wordHead:"啊"}});
        }));

        it('should init core dictionary controller', function(){
            expect(scope.pinyinIndexes).toEqualData(["a","ai","an","ang","ao"]);
            expect(scope.wordHeads).toEqualData([]);
            expect(scope.wordItems).toEqualData([]);

            $httpBackend.flush();
            expect(scope.wordHeads).toEqualData(["啊","擀","炝","阿"]);
            expect(scope.wordItems).toEqualData([{"word":"啊哈"},{"word":"啊"},{"word":"啊哟"},{"word":"啊呀"}]);
        });
    });
});
