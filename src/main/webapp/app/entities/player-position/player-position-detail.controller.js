(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PlayerPositionDetailController', PlayerPositionDetailController);

    PlayerPositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PlayerPosition', 'Player', 'Dict'];

    function PlayerPositionDetailController($scope, $rootScope, $stateParams, previousState, entity, PlayerPosition, Player, Dict) {
        var vm = this;

        vm.playerPosition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:playerPositionUpdate', function(event, result) {
            vm.playerPosition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
