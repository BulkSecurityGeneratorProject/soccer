(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RefereeDeleteController',RefereeDeleteController);

    RefereeDeleteController.$inject = ['$uibModalInstance', 'entity', 'Referee'];

    function RefereeDeleteController($uibModalInstance, entity, Referee) {
        var vm = this;

        vm.referee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Referee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
