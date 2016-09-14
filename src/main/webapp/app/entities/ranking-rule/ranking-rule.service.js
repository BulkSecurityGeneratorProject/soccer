(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('RankingRule', RankingRule);

    RankingRule.$inject = ['$resource'];

    function RankingRule ($resource) {
        var resourceUrl =  'api/ranking-rules/:id';

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
