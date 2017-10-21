(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameRefereeDetailController', GameRefereeDetailController);

    GameRefereeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'GameReferee', 'Game', 'Referee', 'Dict'];

    function GameRefereeDetailController($scope, $rootScope, $stateParams, previousState, entity, GameReferee, Game, Referee, Dict) {
        var vm = this;

        vm.gameReferee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:gameRefereeUpdate', function(event, result) {
            vm.gameReferee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
