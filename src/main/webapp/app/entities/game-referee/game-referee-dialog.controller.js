(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameRefereeDialogController', GameRefereeDialogController);

    GameRefereeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'GameReferee', 'Game', 'Referee', 'Dict'];

    function GameRefereeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, GameReferee, Game, Referee, Dict) {
        var vm = this;

        vm.gameReferee = entity;
        vm.clear = clear;
        vm.save = save;
        vm.games = Game.query();
        vm.referees = Referee.query();
        vm.dicts = Dict.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.gameReferee.id !== null) {
                GameReferee.update(vm.gameReferee, onSaveSuccess, onSaveError);
            } else {
                GameReferee.save(vm.gameReferee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:gameRefereeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
