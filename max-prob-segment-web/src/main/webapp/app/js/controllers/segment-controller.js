'use strict';

/* Controllers */

function SegmentCtrl($scope, segmentService, i18n) {
    $scope.inputText = "";
    $scope.segmentResult = [];
    $scope.isSupportEnglish = true;
    $scope.isEnglishStemming = false;
    $scope.isRecognizePinyin = true;
    $scope.isSegmentMin = true;
    $scope.sendingSegmentRequest = false;
    $scope.segment = function() {
        $scope.sendingSegmentRequest = true;
        var scope = this;
        var param = {
            isSupportEnglish: scope.isSupportEnglish,
            isEnglishStemming: scope.isEnglishStemming,
            isRecognizePinyin: scope.isRecognizePinyin,
            isSegmentMin: scope.isSegmentMin
            };
        segmentService.segment(scope.inputText, param, function(result){
            scope.segmentResult = result;
        }).then(markReceivedResponse, markReceivedResponse);
    }
    $scope.i18n = i18n;

    function markReceivedResponse() {
        $scope.sendingSegmentRequest = false;
    }
}

SegmentCtrl.$inject = ['$scope', 'SegmentService', 'i18n'];
