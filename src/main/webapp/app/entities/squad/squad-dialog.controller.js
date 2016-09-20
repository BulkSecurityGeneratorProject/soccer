(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadDialogController', SquadDialogController);

    SquadDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Squad', 'Game', 'Team'];

    function SquadDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Squad, Game, Team) {
        var vm = this;

        vm.squad = entity;
        vm.clear = clear;
        vm.save = save;
        vm.games = Game.query();
        vm.teams = Team.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.squad.id !== null) {
                Squad.update(vm.squad, onSaveSuccess, onSaveError);
            } else {
                Squad.save(vm.squad, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:squadUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
