(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameDetailController', GameDetailController);

    GameDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Game', 'Timeslot', 'Venue', 'Dict', 'Team'];

    function GameDetailController($scope, $rootScope, $stateParams, previousState, entity, Game, Timeslot, Venue, Dict, Team) {
        var vm = this;

        vm.game = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:gameUpdate', function(event, result) {
            vm.game = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
