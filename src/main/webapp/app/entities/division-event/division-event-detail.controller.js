(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventDetailController', DivisionEventDetailController);

    DivisionEventDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DivisionEvent', 'Season', 'Division', 'Team'];

    function DivisionEventDetailController($scope, $rootScope, $stateParams, previousState, entity, DivisionEvent, Season, Division, Team) {
        var vm = this;

        vm.divisionEvent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:divisionEventUpdate', function(event, result) {
            vm.divisionEvent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
