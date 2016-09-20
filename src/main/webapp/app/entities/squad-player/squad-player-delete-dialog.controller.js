(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadPlayerDeleteController',SquadPlayerDeleteController);

    SquadPlayerDeleteController.$inject = ['$uibModalInstance', 'entity', 'SquadPlayer'];

    function SquadPlayerDeleteController($uibModalInstance, entity, SquadPlayer) {
        var vm = this;

        vm.squadPlayer = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SquadPlayer.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
