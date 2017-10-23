(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('PlayerPosition', PlayerPosition);

    PlayerPosition.$inject = ['$resource'];

    function PlayerPosition ($resource) {
        var resourceUrl =  'api/player-positions/:id';

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
