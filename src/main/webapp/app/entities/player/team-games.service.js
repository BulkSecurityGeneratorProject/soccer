(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('TeamGame', TeamGame);

    TeamGame.$inject = ['$resource', 'DateUtils'];

    function TeamGame ($resource, DateUtils) {
        var resourceUrl =  'api/teams/:id/games';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
        });
    }
})();
