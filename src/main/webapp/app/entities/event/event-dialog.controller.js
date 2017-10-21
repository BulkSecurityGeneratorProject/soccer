(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('EventDialogController', EventDialogController);

    EventDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Event', 'Association', 'DivisionEvent', 'Game', 'Club', 'Team', 'Player', 'Referee', 'Coach'];

    function EventDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Event, Association, DivisionEvent, Game, Club, Team, Player, Referee, Coach) {
        var vm = this;

        vm.event = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.associations = Association.query();
        vm.divisionevents = DivisionEvent.query();
        vm.games = Game.query();
        vm.clubs = Club.query();
        vm.teams = Team.query();
        vm.players = Player.query();
        vm.referees = Referee.query();
        vm.coaches = Coach.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.event.id !== null) {
                Event.update(vm.event, onSaveSuccess, onSaveError);
            } else {
                Event.save(vm.event, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:eventUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.eventTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
