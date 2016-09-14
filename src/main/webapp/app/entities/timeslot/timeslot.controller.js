(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('TimeslotController', TimeslotController);

    TimeslotController.$inject = ['$scope', '$state', 'Timeslot'];

    function TimeslotController ($scope, $state, Timeslot) {
        var vm = this;
        
        vm.timeslots = [];

        loadAll();

        function loadAll() {
            Timeslot.query(function(result) {
                vm.timeslots = result;
            });
        }
    }
})();
