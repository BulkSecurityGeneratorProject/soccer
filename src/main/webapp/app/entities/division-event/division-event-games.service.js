(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionEventGame', DivisionEventGame);

    DivisionEventGame.$inject = ['$resource'];

    function DivisionEventGame ($resource) {
        var resourceUrl =  'api/division-events/:id/games';

        return $resource(resourceUrl, {}, {
        	'saveBatch': { method: 'POST', isArray: true},
            'query': { method: 'GET', isArray: true},
            'update': { method:'PUT' }
        });
    }
})();
