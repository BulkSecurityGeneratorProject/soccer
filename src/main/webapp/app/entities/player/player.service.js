(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Player', Player);

    Player.$inject = ['$resource', 'DateUtils'];

    function Player ($resource, DateUtils) {
        var resourceUrl =  'api/players/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.birth = DateUtils.convertLocalDateFromServer(data.birth);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.birth = DateUtils.convertLocalDateToServer(data.birth);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.birth = DateUtils.convertLocalDateToServer(data.birth);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
