(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Association', Association);

    Association.$inject = ['$resource', 'DateUtils'];

    function Association ($resource, DateUtils) {
        var resourceUrl =  'api/associations/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createAt = DateUtils.convertLocalDateFromServer(data.createAt);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.createAt = DateUtils.convertLocalDateToServer(data.createAt);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.createAt = DateUtils.convertLocalDateToServer(data.createAt);
                    return angular.toJson(data);
                }
            }
        });
    }
})();
