(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('TeamCustom', TeamCustom);

    TeamCustom.$inject = ['$resource'];

    function TeamCustom ($resource) {
        var resourceUrl =  'api/teams/:id/player-statistics';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
