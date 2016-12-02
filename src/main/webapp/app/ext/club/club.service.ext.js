(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('ClubExt', ClubExt);

    ClubExt.$inject = ['$resource', 'DateUtils'];

    function ClubExt ($resource, DateUtils) {
        var resourceUrl =  'api/clubs/:id';

        return $resource(resourceUrl, {}, {
            'queryGames':{url: resourceUrl+'/games', method: 'GET', isArray: true}
        });
    }
})();
