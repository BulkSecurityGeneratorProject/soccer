(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RefereeDialogController', RefereeDialogController);

    RefereeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Referee', 'Association', 'Dict'];

    function RefereeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Referee, Association, Dict) {
        var vm = this;

        vm.referee = entity;
        vm.clear = clear;
        vm.save = save;
        vm.associations = Association.query();
        vm.dicts = Dict.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.referee.id !== null) {
                Referee.update(vm.referee, onSaveSuccess, onSaveError);
            } else {
                Referee.save(vm.referee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:refereeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
