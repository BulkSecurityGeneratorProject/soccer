(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('LineupExt', LineupExt);

    LineupExt.$inject = ['$resource'];

    function LineupExt ($resource) {
        var resourceUrl =  'api/lineups/:id';

        return $resource(resourceUrl, {}, {
        	'saveBatch': { url: 'api/lineups-batch', method: 'POST', isArray: true},
        	'queryByDivisionEventAndTeam' : {url : 'api/lineups/division-event/:did/team/:tid', method: 'GET', isArray: true}
        });
    }
})();
