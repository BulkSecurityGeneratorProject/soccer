(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('SquadPlayer', SquadPlayer);

    SquadPlayer.$inject = ['$resource'];

    function SquadPlayer ($resource) {
        var resourceUrl =  'api/squad-players/:id';

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
