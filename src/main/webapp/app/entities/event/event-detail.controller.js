(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('EventDetailController', EventDetailController);

    EventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Event', 'Association', 'DivisionEvent', 'Game', 'Club', 'Team', 'Player', 'Referee', 'Coach'];

    function EventDetailController($scope, $rootScope, $stateParams, previousState, entity, Event, Association, DivisionEvent, Game, Club, Team, Player, Referee, Coach) {
        var vm = this;

        vm.event = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:eventUpdate', function(event, result) {
            vm.event = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
