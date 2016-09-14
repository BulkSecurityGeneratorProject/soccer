(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionDeleteController',DivisionDeleteController);

    DivisionDeleteController.$inject = ['$uibModalInstance', 'entity', 'Division'];

    function DivisionDeleteController($uibModalInstance, entity, Division) {
        var vm = this;

        vm.division = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Division.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
