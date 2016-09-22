(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('ResultDataSquad', ResultDataSquad);

    ResultDataSquad.$inject = ['$resource'];

    function ResultDataSquad ($resource) {
        var resourceUrl =  'api/squads/:id/result-data';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'save': {
                method: 'POST',
                isArray:true,
                transformRequest: function (data) {
                    return angular.toJson(data);
                }
            }
        });
    }
})();
