(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('GameSquadQuery', GameSquadQuery);

    GameSquadQuery.$inject = ['$resource'];

    function GameSquadQuery ($resource) {
        var resourceUrl =  'api/games/:id/squad/:tid';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
