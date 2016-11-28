(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('LineupDeleteController',LineupDeleteController);

    LineupDeleteController.$inject = ['$uibModalInstance', 'entity', 'Lineup'];

    function LineupDeleteController($uibModalInstance, entity, Lineup) {
        var vm = this;

        vm.lineup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Lineup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
