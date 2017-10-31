(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('CatalogController', CatalogController);

    CatalogController.$inject = ['$scope', '$state', 'Catalog'];

    function CatalogController ($scope, $state, Catalog) {
        var vm = this;
        
        vm.catalogs = [];

        loadAll();

        function loadAll() {
            Catalog.query(function(result) {
                vm.catalogs = result;
            });
        }
    }
})();
