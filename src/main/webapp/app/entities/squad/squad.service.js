(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Squad', Squad);

    Squad.$inject = ['$resource'];

    function Squad ($resource) {
        var resourceUrl =  'api/squads/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
