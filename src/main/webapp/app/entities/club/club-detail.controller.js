(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubDetailController', ClubDetailController);

    ClubDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Club', 'Association'];

    function ClubDetailController($scope, $rootScope, $stateParams, previousState, entity, Club, Association) {
        var vm = this;

        vm.club = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:clubUpdate', function(event, result) {
            vm.club = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
