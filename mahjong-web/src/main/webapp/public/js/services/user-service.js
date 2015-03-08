'use strict';

corpusEditorServices.
    factory('UserService', function ($http) {
        return {
            getCurrentUser: function(callback) {
                $http.get('/admin/user/current').success(callback).error(callback(null));
            }
        };
    });
