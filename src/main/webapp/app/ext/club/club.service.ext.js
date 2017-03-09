(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('ClubExt', ClubExt);

    ClubExt.$inject = ['$resource', 'DateUtils'];

    function ClubExt ($resource, DateUtils) {
        var resourceUrl =  'api/clubs/:id';

        return $resource(resourceUrl, {}, {
            'queryGames':{url: resourceUrl+'/games', method: 'GET', isArray: true},
            'queryNextGame':{url: resourceUrl+'/nextgame', method: 'GET'},
            'queryTeams':{url: resourceUrl+'/teams', method: 'GET', isArray: true},
            'queryPlayers':{url: resourceUrl+'/players', method: 'GET', isArray: true},
            'queryDivisionEvents':{url: resourceUrl+'/division-events', method: 'GET', isArray: true}
        });
    }
})();
