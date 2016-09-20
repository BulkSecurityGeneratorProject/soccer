(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventTableController', DivisionEventTableController);

    DivisionEventTableController.$inject = ['$scope', '$state', 'DivisionEventTable'];

    function DivisionEventTableController ($scope, $state, DivisionEventTable) {
        var vm = this;
        
        vm.divisionEvents = [];

        loadAll($state.params);

        function loadAll(params) {
        	DivisionEventTable.query(params,function(result) {
                vm.divisionEvents = result;
            });
        }
    }
})();
