'use strict';

/* Controllers */

function SegmentCtrl($scope, segmentService, i18n) {
    $scope.inputText = "";
    $scope.segmentResult = [];
    $scope.isSupportEnglish = true
    $scope.isRecognizePinyin = true
    $scope.isSegmentMin = true
    $scope.segment = function() {
        var scope = this;
        var param = {
            isSupportEnglish: scope.isSupportEnglish,
            isRecognizePinyin: scope.isRecognizePinyin,
            isSegmentMin: scope.isSegmentMin
            };
        segmentService.segment(scope.inputText, param, function(result){
            scope.segmentResult = result;
        });
    }
    $scope.i18n = i18n;
}

SegmentCtrl.$inject = ['$scope', 'SegmentService', 'i18n'];
