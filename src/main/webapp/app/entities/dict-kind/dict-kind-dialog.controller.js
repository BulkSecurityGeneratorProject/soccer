(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictKindDialogController', DictKindDialogController);

    DictKindDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DictKind'];

    function DictKindDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DictKind) {
        var vm = this;

        vm.dictKind = entity;
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
            if (vm.dictKind.id !== null) {
                DictKind.update(vm.dictKind, onSaveSuccess, onSaveError);
            } else {
                DictKind.save(vm.dictKind, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:dictKindUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
