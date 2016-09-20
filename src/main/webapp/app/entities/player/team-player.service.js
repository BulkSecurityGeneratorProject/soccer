(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('TeamPlayer', TeamPlayer);

    TeamPlayer.$inject = ['$resource', 'DateUtils'];

    function TeamPlayer ($resource, DateUtils) {
        var resourceUrl =  'api/team/:id/players';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
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
