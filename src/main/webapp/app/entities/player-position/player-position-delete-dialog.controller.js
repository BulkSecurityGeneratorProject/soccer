(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PlayerPositionDeleteController',PlayerPositionDeleteController);

    PlayerPositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'PlayerPosition'];

    function PlayerPositionDeleteController($uibModalInstance, entity, PlayerPosition) {
        var vm = this;

        vm.playerPosition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PlayerPosition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
