(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('GameReferee', GameReferee);

    GameReferee.$inject = ['$resource'];

    function GameReferee ($resource) {
        var resourceUrl =  'api/game-referees/:id';

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
