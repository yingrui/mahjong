corpusEditorDirectives.directive('autoComplete', function($timeout) {
    return function(scope, iElement, iAttrs) {
        var options = {
            source: scope[iAttrs.uiItems],
            select: function() {
                $timeout(function() {
                    iElement.trigger('input');
                }, 0);
            },
            change: function( event, ui ) {
                var fn = iAttrs.uiChange;
                if(fn != null && fn != undefined) {
                    var onChange = 'scope.'+fn+'(ui);';
                    eval(onChange);
                }
            }
        };
        iElement.autocomplete(options);
    };
});