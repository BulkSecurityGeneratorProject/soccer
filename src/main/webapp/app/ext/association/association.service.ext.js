(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('AssociationExt', AssociationExt);

    AssociationExt.$inject = ['$resource'];

    function AssociationExt ($resource) {
        var resourceUrl =  'api/associations/:id';

        return $resource(resourceUrl, {}, {
            'queryClubs':{url: resourceUrl+'/clubs', method: 'GET', isArray: true},
            'queryDivisions':{url: resourceUrl+'/divisions', method: 'GET', isArray: true},
            'queryPlayers':{url: resourceUrl+'/players', method: 'GET', isArray: true},
            'queryDivisionEvents':{url: resourceUrl+'/division-events', method: 'GET', isArray: true},
            'queryGames':{url: resourceUrl+'/games', method: 'GET', isArray: true},
            'queryAssociationFixtures':{url: resourceUrl+'/fixtures', method: 'GET', isArray: true}
        });
    }
})();
