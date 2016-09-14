(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultFieldDialogController', ResultFieldDialogController);

    ResultFieldDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ResultField'];

    function ResultFieldDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ResultField) {
        var vm = this;

        vm.resultField = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resultField.id !== null) {
                ResultField.update(vm.resultField, onSaveSuccess, onSaveError);
            } else {
                ResultField.save(vm.resultField, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:resultFieldUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
