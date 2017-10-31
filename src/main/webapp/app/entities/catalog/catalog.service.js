(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Catalog', Catalog);

    Catalog.$inject = ['$resource'];

    function Catalog ($resource) {
        var resourceUrl =  'api/catalogs/:id';

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
