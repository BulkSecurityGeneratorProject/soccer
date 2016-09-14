(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('Timeslot', Timeslot);

    Timeslot.$inject = ['$resource'];

    function Timeslot ($resource) {
        var resourceUrl =  'api/timeslots/:id';

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
