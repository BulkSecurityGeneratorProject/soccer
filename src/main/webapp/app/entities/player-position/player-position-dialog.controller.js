(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PlayerPositionDialogController', PlayerPositionDialogController);

    PlayerPositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PlayerPosition', 'Player', 'Dict'];

    function PlayerPositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PlayerPosition, Player, Dict) {
        var vm = this;

        vm.playerPosition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.players = Player.query();
        vm.dicts = Dict.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.playerPosition.id !== null) {
                PlayerPosition.update(vm.playerPosition, onSaveSuccess, onSaveError);
            } else {
                PlayerPosition.save(vm.playerPosition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:playerPositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
