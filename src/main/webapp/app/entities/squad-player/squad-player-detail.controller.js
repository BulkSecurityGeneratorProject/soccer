(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadPlayerDetailController', SquadPlayerDetailController);

    SquadPlayerDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SquadPlayer', 'Squad', 'Player'];

    function SquadPlayerDetailController($scope, $rootScope, $stateParams, previousState, entity, SquadPlayer, Squad, Player) {
        var vm = this;

        vm.squadPlayer = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:squadPlayerUpdate', function(event, result) {
            vm.squadPlayer = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
