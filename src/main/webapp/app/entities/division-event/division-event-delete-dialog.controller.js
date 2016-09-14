(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventDeleteController',DivisionEventDeleteController);

    DivisionEventDeleteController.$inject = ['$uibModalInstance', 'entity', 'DivisionEvent'];

    function DivisionEventDeleteController($uibModalInstance, entity, DivisionEvent) {
        var vm = this;

        vm.divisionEvent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DivisionEvent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
