(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('TeamDialogController', TeamDialogController);

    TeamDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Team', 'Club', 'Dict', 'DivisionEvent'];

    function TeamDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Team, Club, Dict, DivisionEvent) {
        var vm = this;

        vm.team = entity;
        vm.clear = clear;
        vm.save = save;
        vm.clubs = Club.query();
        vm.dicts = Dict.query();
        vm.divisionevents = DivisionEvent.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.team.id !== null) {
                Team.update(vm.team, onSaveSuccess, onSaveError);
            } else {
                Team.save(vm.team, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:teamUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
