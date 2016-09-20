(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionEventAssistRanking', DivisionEventAssistRanking);
    
    DivisionEventAssistRanking.$inject = ['$resource'];

    function DivisionEventAssistRanking ($resource) {
        var resourceUrl =  'api/division-event/:id/assist-ranking';
        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
