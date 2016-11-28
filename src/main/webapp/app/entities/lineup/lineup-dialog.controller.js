(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('LineupDialogController', LineupDialogController);

    LineupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Lineup', 'Player', 'Team', 'DivisionEvent', 'Dict'];

    function LineupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Lineup, Player, Team, DivisionEvent, Dict) {
        var vm = this;

        vm.lineup = entity;
        vm.clear = clear;
        vm.save = save;
        vm.players = Player.query();
        vm.teams = Team.query();
        vm.divisionevents = DivisionEvent.query();
        vm.dicts = Dict.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.lineup.id !== null) {
                Lineup.update(vm.lineup, onSaveSuccess, onSaveError);
            } else {
                Lineup.save(vm.lineup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:lineupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
