(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionEventTable', DivisionEventTable);
    
    DivisionEventTable.$inject = ['$resource'];

    function DivisionEventTable ($resource) {
        var resourceUrl =  'api/division-event/:id/table';
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
