(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionEventGoalRanking', DivisionEventGoalRanking);
    
    DivisionEventGoalRanking.$inject = ['$resource'];

    function DivisionEventGoalRanking ($resource) {
        var resourceUrl =  'api/division-event/:id/goal-ranking';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
