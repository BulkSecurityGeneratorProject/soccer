(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('AssociationClub', AssociationClub);

    AssociationClub.$inject = ['$resource'];

    function AssociationClub ($resource) {
        var resourceUrl =  'api/associations/:id/clubs';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
