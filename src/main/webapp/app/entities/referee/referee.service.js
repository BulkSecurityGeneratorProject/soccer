(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Referee', Referee);

    Referee.$inject = ['$resource'];

    function Referee ($resource) {
        var resourceUrl =  'api/referees/:id';

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
