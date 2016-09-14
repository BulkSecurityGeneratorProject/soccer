(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('VenueController', VenueController);

    VenueController.$inject = ['$scope', '$state', 'Venue'];

    function VenueController ($scope, $state, Venue) {
        var vm = this;
        
        vm.venues = [];

        loadAll();

        function loadAll() {
            Venue.query(function(result) {
                vm.venues = result;
            });
        }
    }
})();
