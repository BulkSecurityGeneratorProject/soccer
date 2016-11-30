(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DivisionExt', DivisionExt);

    DivisionExt.$inject = ['$resource', 'DateUtils'];

    function DivisionExt ($resource, DateUtils) {
        var resourceUrl =  'api/divisions/:id';

        return $resource(resourceUrl, {}, {
            'queryDivisions':{url: 'api/divisions/:id/division-events', method: 'GET', isArray: true}
        });
    }
})();
