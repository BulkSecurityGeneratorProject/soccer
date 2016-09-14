(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SeasonDeleteController',SeasonDeleteController);

    SeasonDeleteController.$inject = ['$uibModalInstance', 'entity', 'Season'];

    function SeasonDeleteController($uibModalInstance, entity, Season) {
        var vm = this;

        vm.season = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Season.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
