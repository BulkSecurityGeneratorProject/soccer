(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventDialogController', DivisionEventDialogController);

    DivisionEventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'DivisionEvent', 'Season', 'Division', 'Team'];

    function DivisionEventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, DivisionEvent, Season, Division, Team) {
        var vm = this;

        vm.divisionEvent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.seasons = Season.query();
        vm.divisions = Division.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.divisionEvent.id !== null) {
                DivisionEvent.update(vm.divisionEvent, onSaveSuccess, onSaveError);
            } else {
                DivisionEvent.save(vm.divisionEvent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:divisionEventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
