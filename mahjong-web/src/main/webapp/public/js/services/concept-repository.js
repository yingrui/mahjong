corpusEditorServices.
    factory('ConceptRepository', function ($http) {

        var allConcepts = [];

        $http.get('/concept').success(function(data){
            allConcepts = data;
        });

        return {
            getAll: function(onSuccess) {
                $http.get('/concept').success(function(data){
                    allConcepts = data;
                    onSuccess(allConcepts);
                });
            },
            getByName: function(name) {
                return _.find(allConcepts, function(concept){
                    return concept.name == name;
                });
            }
        };
    });
