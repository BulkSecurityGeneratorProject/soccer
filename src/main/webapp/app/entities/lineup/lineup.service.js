(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Lineup', Lineup);

    Lineup.$inject = ['$resource'];

    function Lineup ($resource) {
        var resourceUrl =  'api/lineups/:id';

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
