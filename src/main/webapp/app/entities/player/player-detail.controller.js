(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PlayerDetailController', PlayerDetailController);

    PlayerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Player', 'Team', 'Dict', 'PlayerPosition'];

    function PlayerDetailController($scope, $rootScope, $stateParams, previousState, entity, Player, Team, Dict, PlayerPosition) {
        var vm = this;

        vm.player = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:playerUpdate', function(event, result) {
            vm.player = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
