(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationDialogController', AssociationDialogController);

    AssociationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Association'];

    function AssociationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Association) {
        var vm = this;

        vm.association = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.association.id !== null) {
                Association.update(vm.association, onSaveSuccess, onSaveError);
            } else {
                Association.save(vm.association, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:associationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
