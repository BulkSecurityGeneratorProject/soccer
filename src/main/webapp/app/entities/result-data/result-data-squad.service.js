(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('ResultDataSquad', ResultDataSquad);

    ResultDataSquad.$inject = ['$resource'];

    function ResultDataSquad ($resource) {
        var resourceUrl =  'api/squads/:id/result-data';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
