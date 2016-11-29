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
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startAt = DateUtils.convertDateTimeToServer(data.startAt);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startAt = DateUtils.convertDateTimeToServer(data.startAt);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
