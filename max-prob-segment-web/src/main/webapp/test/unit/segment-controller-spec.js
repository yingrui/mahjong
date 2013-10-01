'use strict';

describe('Segment Controller', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(module('corpusEditorServices'));

    describe('SegmentCtrl', function(){
        var scope, ctrl, $httpBackend;

        beforeEach(inject(function(_$httpBackend_, $rootScope, $controller, SegmentService, i18n) {
            $httpBackend = _$httpBackend_;
            $httpBackend.expectPOST('/api/segment?&segment.lang.en=true&segment.lang.en.stemming=false&recognize.pinyin=true&minimize.word=true').
                respond([{name: 'word1'}, {name: 'word2'}]);

            scope = $rootScope.$new();
            ctrl = $controller(SegmentCtrl, {"$scope": scope, "segmentService": SegmentService, "i18n": i18n});
        }));

        it('should init segment controller', function(){
            expect(scope.inputText).toBe("");
            expect(scope.segmentResult.length).toBe(0);
            expect(scope.isSupportEnglish).toBeTruthy();
            expect(scope.isEnglishStemming).toBeFalsy();
            expect(scope.isRecognizePinyin).toBeTruthy();
            expect(scope.isSegmentMin).toBeTruthy();
        });


        it('should send segment request and receive result', function() {
            scope.segment();
            $httpBackend.flush();

            expect(scope.segmentResult).toEqualData(
                [{name: 'word1'}, {name: 'word2'}]);
        });

        it('should disable segment button when send segment request', function(){
            scope.segment();
            expect(scope.sendingSegmentRequest).toBeTruthy()
            $httpBackend.flush();
            expect(scope.sendingSegmentRequest).toBeFalsy()
        });
    });
});
