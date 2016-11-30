(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('PlayerExt', PlayerExt);

    PlayerExt.$inject = ['$resource'];

    function PlayerExt ($resource) {
        var resourceUrl =  'api/players/:id/career';

        return $resource(resourceUrl, {}, {
            'queryCareer': { method: 'GET', isArray: true}
        });
    }
})();
