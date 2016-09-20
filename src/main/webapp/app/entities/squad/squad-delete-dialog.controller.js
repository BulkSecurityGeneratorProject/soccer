(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadDeleteController',SquadDeleteController);

    SquadDeleteController.$inject = ['$uibModalInstance', 'entity', 'Squad'];

    function SquadDeleteController($uibModalInstance, entity, Squad) {
        var vm = this;

        vm.squad = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Squad.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
