(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Division', Division);

    Division.$inject = ['$resource', 'DateUtils'];

    function Division ($resource, DateUtils) {
        var resourceUrl =  'api/divisions/:id';

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
            },
            'queryDivisions':{url: 'api/divisions/:id/division-events', method: 'GET', isArray: true}
        });
    }
})();
