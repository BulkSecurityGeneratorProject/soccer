(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionEvent', DivisionEvent);

    DivisionEvent.$inject = ['$resource'];

    function DivisionEvent ($resource) {
        var resourceUrl =  'api/division-events/:id';

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
