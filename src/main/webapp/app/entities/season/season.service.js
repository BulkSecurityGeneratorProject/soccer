(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Season', Season);

    Season.$inject = ['$resource', 'DateUtils'];

    function Season ($resource, DateUtils) {
        var resourceUrl =  'api/seasons/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startAt = DateUtils.convertLocalDateFromServer(data.startAt);
                        data.endAt = DateUtils.convertLocalDateFromServer(data.endAt);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.startAt = DateUtils.convertLocalDateToServer(data.startAt);
                    data.endAt = DateUtils.convertLocalDateToServer(data.endAt);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.startAt = DateUtils.convertLocalDateToServer(data.startAt);
                    data.endAt = DateUtils.convertLocalDateToServer(data.endAt);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
