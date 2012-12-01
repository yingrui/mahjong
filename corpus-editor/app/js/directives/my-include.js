'use strict';

/* Directives */
corpusEditorDirectives.directive('myInclude',
    ['$http', '$templateCache', '$anchorScroll', '$compile', '$route',
    function ($http, $templateCache, $anchorScroll, $compile, $route) {
        return {
            restrict:'E',
            terminal:true,
            compile:function (element, attr) {
                var srcExp = attr.src,
                    onloadExp = attr.onload || '';

                return function (scope, element) {
                    var clearContent = function () {
                        element.html('');
                    };

                    scope.$watch(srcExp, function () {
                        var src = $route.current[srcExp];

                        if (src) {
                            $http.get(src, {cache:$templateCache}).success(function (response) {

                                element.html(response);
                                $compile(element.contents())(scope);

                                scope.$emit('$includeContentLoaded');
                                scope.$eval(onloadExp);
                            }).error(function () {
                                    clearContent();
                                });
                        } else clearContent();
                    });
                };
            }
        };
    }]
);
