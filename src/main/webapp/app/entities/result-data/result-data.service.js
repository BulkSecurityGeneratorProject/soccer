(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('ResultData', ResultData);

    ResultData.$inject = ['$resource'];

    function ResultData ($resource) {
        var resourceUrl =  'api/result-data/:id';

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
