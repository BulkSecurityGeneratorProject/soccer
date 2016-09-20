(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadPlayerDialogController', SquadPlayerDialogController);

    SquadPlayerDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SquadPlayer', 'Squad', 'Player'];

    function SquadPlayerDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SquadPlayer, Squad, Player) {
        var vm = this;

        vm.squadPlayer = entity;
        vm.clear = clear;
        vm.save = save;
        vm.squads = Squad.query();
        vm.players = Player.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.squadPlayer.id !== null) {
                SquadPlayer.update(vm.squadPlayer, onSaveSuccess, onSaveError);
            } else {
                SquadPlayer.save(vm.squadPlayer, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:squadPlayerUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
