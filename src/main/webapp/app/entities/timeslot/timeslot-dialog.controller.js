(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('TimeslotDialogController', TimeslotDialogController);

    TimeslotDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Timeslot', 'Dict'];

    function TimeslotDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Timeslot, Dict) {
        var vm = this;

        vm.timeslot = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dicts = Dict.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.timeslot.id !== null) {
                Timeslot.update(vm.timeslot, onSaveSuccess, onSaveError);
            } else {
                Timeslot.save(vm.timeslot, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:timeslotUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
