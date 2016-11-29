(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DictKind', DictKind);

    DictKind.$inject = ['$resource'];

    function DictKind ($resource) {
        var resourceUrl =  'api/dict-kinds/:id';

        return $resource(resourceUrl, {}, {
        	'queryDicts': { url: resourceUrl+'/dicts', method: 'GET', isArray: true},
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
