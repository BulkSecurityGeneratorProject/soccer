(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('ResultField', ResultField);

    ResultField.$inject = ['$resource'];

    function ResultField ($resource) {
        var resourceUrl =  'api/result-fields/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
