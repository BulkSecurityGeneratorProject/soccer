(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictDialogController', DictDialogController);

    DictDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Dict', 'DictKind'];

    function DictDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Dict, DictKind) {
        var vm = this;

        vm.dict = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dictkinds = DictKind.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.dict.id !== null) {
                Dict.update(vm.dict, onSaveSuccess, onSaveError);
            } else {
                Dict.save(vm.dict, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:dictUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
