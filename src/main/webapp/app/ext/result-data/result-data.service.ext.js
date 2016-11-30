(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('ResultDataExt', ResultDataExt);

    ResultDataExt.$inject = ['$resource'];

    function ResultDataExt ($resource) {
        var resourceUrl =  'api/squads/:id/result-data';

        return $resource(resourceUrl, {}, {
            'querySquadGameResult': { method: 'GET', isArray: true},
            'saveGameResult': {
            	method: 'POST',
            	isArray: true,
            	transformRequest: function(data){
            		return angular.toJson(data);
            	}
            }
        });
    }
})();
