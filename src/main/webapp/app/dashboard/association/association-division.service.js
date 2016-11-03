(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('AssociationDivision', AssociationDivision);

    AssociationDivision.$inject = ['$resource'];

    function AssociationDivision ($resource) {
        var resourceUrl =  'api/associations/:id/divisions';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
