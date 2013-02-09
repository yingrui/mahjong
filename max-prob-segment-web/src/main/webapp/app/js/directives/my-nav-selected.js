'use strict';

/* Directives */
corpusEditorDirectives.directive('myNavSelected',
    ['$location', function ($location) {
        return {
            restrict:'A',
            scope:false,
            link:function (scope, iElement, iAttrs) {
                var nav = iAttrs.ngClick;
                var rootPath = $location.path().split("/")[1];
                if(nav.indexOf(rootPath) > 0) {
                    $(iElement).addClass('selected');
                }
            }
        };
    }]
);
