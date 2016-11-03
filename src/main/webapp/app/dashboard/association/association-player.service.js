(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('AssociationPlayer', AssociationPlayer);

    AssociationPlayer.$inject = ['$resource'];

    function AssociationPlayer ($resource) {
        var resourceUrl =  'api/associations/:id/players';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();