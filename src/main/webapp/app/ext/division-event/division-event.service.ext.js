(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionEventExt', DivisionEventExt);

    DivisionEventExt.$inject = ['$resource'];

    function DivisionEventExt ($resource) {
        var resourceUrl =  'api/division-events/:id/games';

        return $resource(resourceUrl, {}, {
        	'saveGameBatch': { method: 'POST', isArray: true},
            'queryGames': { method: 'GET', isArray: true},
            'queryTeams': {url: 'api/division-events/:id/teams', method: 'GET', isArray: true}
        });
    }
})();
