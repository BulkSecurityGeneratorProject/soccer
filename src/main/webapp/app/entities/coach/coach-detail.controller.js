(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('CoachDetailController', CoachDetailController);

    CoachDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Coach', 'Dict', 'Association', 'Team'];

    function CoachDetailController($scope, $rootScope, $stateParams, previousState, entity, Coach, Dict, Association, Team) {
        var vm = this;

        vm.coach = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:coachUpdate', function(event, result) {
            vm.coach = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
