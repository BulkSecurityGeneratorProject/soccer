(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventTableController', DivisionEventTableController);

    DivisionEventTableController.$inject = ['$scope', '$state', 'DivisionEventTable'];

    function DivisionEventTableController ($scope, $state, DivisionEventTable) {
        var vm = this;
        
        vm.divisionEvents = [];

        loadAll();

        function loadAll() {
        	DivisionEventTable.query(function(result) {
                vm.divisionEvents = result;
            });
        }
    }
})();
