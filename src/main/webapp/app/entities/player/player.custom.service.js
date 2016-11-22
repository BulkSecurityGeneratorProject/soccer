(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('PlayerCustom', PlayerCustom);

    PlayerCustom.$inject = ['$resource'];

    function PlayerCustom ($resource) {
        var resourceUrl =  'api/players/:id/career';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
