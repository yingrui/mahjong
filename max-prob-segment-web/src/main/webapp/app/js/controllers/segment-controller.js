'use strict';

/* Controllers */

function SegmentCtrl($scope, segmentService, i18n) {
    $scope.inputText = "";
    $scope.segmentResult = [];
    $scope.segment = function() {
        var scope = this;
        console.log(scope.inputText);
        segmentService.segment(scope.inputText, function(result){
            scope.segmentResult = result;
        });
    }
    $scope.i18n = i18n;
}

SegmentCtrl.$inject = ['$scope', 'SegmentService', 'i18n'];
