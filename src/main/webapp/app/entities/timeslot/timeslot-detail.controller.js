(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('TimeslotDetailController', TimeslotDetailController);

    TimeslotDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Timeslot', 'Dict'];

    function TimeslotDetailController($scope, $rootScope, $stateParams, previousState, entity, Timeslot, Dict) {
        var vm = this;

        vm.timeslot = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:timeslotUpdate', function(event, result) {
            vm.timeslot = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
