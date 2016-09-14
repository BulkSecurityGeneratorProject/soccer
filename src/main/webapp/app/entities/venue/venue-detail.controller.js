(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('VenueDetailController', VenueDetailController);

    VenueDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Venue'];

    function VenueDetailController($scope, $rootScope, $stateParams, previousState, entity, Venue) {
        var vm = this;

        vm.venue = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:venueUpdate', function(event, result) {
            vm.venue = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
