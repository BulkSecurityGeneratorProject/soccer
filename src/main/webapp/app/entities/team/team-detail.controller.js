(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('TeamDetailController', TeamDetailController);

    TeamDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Team', 'Club', 'Dict', 'DivisionEvent'];

    function TeamDetailController($scope, $rootScope, $stateParams, previousState, entity, Team, Club, Dict, DivisionEvent) {
        var vm = this;

        vm.team = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:teamUpdate', function(event, result) {
            vm.team = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
