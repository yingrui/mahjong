corpusEditorDirectives.directive('myConceptEditor', ['ConceptRepository', '$timeout', 'PartOfSpeechRepository', function (conceptRepository, $timeout, partOfSpeechRepository) {

    function adaptToAutoCompleteSource(data) {
        return _.map(data, function(concept) {
            var label = concept.note + ', ' + concept.name;
            return {'label': label, 'value': concept.note};
        });
    }

    function parseLabel(ui) {
        var label = ui.label;
        return label.substring(label.indexOf(', ') + 2);
    }

    return {
        priority: 0,
        templateUrl: '/app/partials/directives/my-concept-editor.htm',
        replace: false,
        restrict: 'E',
        compile: function(){
            return {
                pre: function(scope, iElement, iAttrs, controller) {
                    var concept = scope[iAttrs.ngModel];
                    scope.source = adaptToAutoCompleteSource(scope.allConcepts);
                    scope.selectConcept = function(ui) {
                        var conceptName = parseLabel(ui.item);
                        var conceptFromRepo = conceptRepository.getByName(conceptName);
                        concept.id = conceptFromRepo.id;
                        concept.name = conceptFromRepo.name;
                        concept.note = conceptFromRepo.note;
                        concept.partOfSpeech = conceptFromRepo.partOfSpeech;
                        concept.parentId = conceptFromRepo.parentId;
                        scope.$apply();
                    }
                }
            }
        },
        link: function (scope, iElement, iAttrs) {
        }
    };

}]);
