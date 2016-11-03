(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionEventTeam', DivisionEventTeam);

    DivisionEventTeam.$inject = ['$resource'];

    function DivisionEventTeam ($resource) {
        var resourceUrl =  'api/division-events/:id/teams';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'update': { method:'PUT' }
        });
    }
})();
