(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Game', Game);

    Game.$inject = ['$resource', 'DateUtils'];

    function Game ($resource, DateUtils) {
        var resourceUrl =  'api/games/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startAt = DateUtils.convertDateTimeFromServer(data.startAt);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
