'use strict';

describe('Http Backend Mock', function() {

    beforeEach(function(){
        this.addMatchers({
            toEqualData: function(expected) {
                return angular.equals(this.actual, expected);
            }
        });
    });

    beforeEach(module('corpusEditorServices'));

    describe('service $httpBackend and $http', function(){
        var $httpBackend, $resource, $http;

        beforeEach(inject(function(_$httpBackend_, $injector) {
            $httpBackend = _$httpBackend_;
            $resource = $injector.get('$resource');
            $http = $injector.get('$http');
        }));

        it('should return object via $resource', function() {
            $httpBackend.expectGET('object/article1.json').
                respond({segmentResult:[[{name: 'word1'}]]});

            var request = $resource('object/:articleId.json',{articleId:'article1'}, {method:"Get"});
            var response = request.get();
            $httpBackend.flush();
            expect(response).toEqualData({segmentResult:[[{name: 'word1'}]]});
        });

        it('should return array via $resource', function() {
            $httpBackend.expectGET('array/value').
                respond([{name: 'word1'}, {name: 'word2'}]);

            var resource = $resource('array/value',{}, {query: {method:"Get", isArray: true}});
            var response = resource.query();
            expect(response).toEqual([]);
            $httpBackend.flush();
            expect(response).toEqualData(
                [{name: 'word1'}, {name: 'word2'}]);
        });

        it('should return array via $resource', function() {
            $httpBackend.expectGET('string/value').
                respond("string");

            $http({method:'GET', url:'string/value'})
                .success(function(data, status, headers, config){
                    expect(data).toEqual("string");
                });
            $httpBackend.flush();
        });
    });
});
