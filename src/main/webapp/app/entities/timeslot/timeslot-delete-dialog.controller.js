(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('TimeslotDeleteController',TimeslotDeleteController);

    TimeslotDeleteController.$inject = ['$uibModalInstance', 'entity', 'Timeslot'];

    function TimeslotDeleteController($uibModalInstance, entity, Timeslot) {
        var vm = this;

        vm.timeslot = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Timeslot.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
