(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventController', DivisionEventController);

    DivisionEventController.$inject = ['$scope', '$state', 'DivisionEvent'];

    function DivisionEventController ($scope, $state, DivisionEvent) {
        var vm = this;
        
        vm.divisionEvents = [];

        loadAll();

        function loadAll() {
            DivisionEvent.query(function(result) {
                vm.divisionEvents = result;
            });
        }
    }
})();
