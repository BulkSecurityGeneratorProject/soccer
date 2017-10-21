(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RefereeDetailController', RefereeDetailController);

    RefereeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Referee', 'Association', 'Dict'];

    function RefereeDetailController($scope, $rootScope, $stateParams, previousState, entity, Referee, Association, Dict) {
        var vm = this;

        vm.referee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:refereeUpdate', function(event, result) {
            vm.referee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
