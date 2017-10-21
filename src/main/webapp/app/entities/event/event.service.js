(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Event', Event);

    Event.$inject = ['$resource', 'DateUtils'];

    function Event ($resource, DateUtils) {
        var resourceUrl =  'api/events/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.eventTime = DateUtils.convertLocalDateFromServer(data.eventTime);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.eventTime = DateUtils.convertLocalDateToServer(data.eventTime);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.eventTime = DateUtils.convertLocalDateToServer(data.eventTime);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
