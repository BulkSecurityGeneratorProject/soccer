(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('LineupDetailController', LineupDetailController);

    LineupDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Lineup', 'Player', 'Team', 'DivisionEvent'];

    function LineupDetailController($scope, $rootScope, $stateParams, previousState, entity, Lineup, Player, Team, DivisionEvent) {
        var vm = this;

        vm.lineup = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:lineupUpdate', function(event, result) {
            vm.lineup = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
