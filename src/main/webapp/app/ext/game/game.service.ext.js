(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('GameExt', GameExt);

    GameExt.$inject = ['$resource'];

    function GameExt ($resource) {
        var resourceUrl =  'api/games/squad';

        return $resource(resourceUrl, {}, {
            'queryGameSquad': { method: 'GET', isArray: true},
            'getGameSquad': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'updateGameSquad': {
                method: 'PUT',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'saveGameSquad': {
                method: 'POST',
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            },
            'queryGameSquadByTeam': {url: 'api/games/:id/squad/:tid', method: 'GET', isArray: true}
        });
    }
})();
