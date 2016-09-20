(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadDetailController', SquadDetailController);

    SquadDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Squad', 'Game', 'Team'];

    function SquadDetailController($scope, $rootScope, $stateParams, previousState, entity, Squad, Game, Team) {
        var vm = this;

        vm.squad = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:squadUpdate', function(event, result) {
            vm.squad = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
