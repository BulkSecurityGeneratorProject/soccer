(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('CatalogDeleteController',CatalogDeleteController);

    CatalogDeleteController.$inject = ['$uibModalInstance', 'entity', 'Catalog'];

    function CatalogDeleteController($uibModalInstance, entity, Catalog) {
        var vm = this;

        vm.catalog = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Catalog.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
