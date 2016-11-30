(function() {
    'use strict';
    angular
        .module('soccerApp')
        .factory('DictKindExt', DictKindExt);

    DictKindExt.$inject = ['$resource'];

    function DictKindExt ($resource) {
        var resourceUrl =  'api/dict-kinds/:id';

        return $resource(resourceUrl, {}, {
        	'queryDicts': { url: resourceUrl+'/dicts', method: 'GET', isArray: true}
        });
    }
})();
