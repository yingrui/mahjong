'use strict';

/* Controllers */

function SegmentCtrl($scope, segmentService) {
    $scope.inputText = "";
    $scope.segmentResult = [];
    $scope.segment = function() {
        var scope = this;
        segmentService.segment(scope.inputText, function(result){
            scope.segmentResult = result;
        });
    }
}

SegmentCtrl.$inject = ['$scope', 'SegmentService'];
