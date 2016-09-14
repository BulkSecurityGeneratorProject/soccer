(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Dict', Dict);

    Dict.$inject = ['$resource'];

    function Dict ($resource) {
        var resourceUrl =  'api/dicts/:id';

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
