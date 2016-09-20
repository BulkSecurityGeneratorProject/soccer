(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventTableController', DivisionEventTableController);

    DivisionEventTableController.$inject = ['$scope','$state',  'DivisionEvent', 'DivisionEventTable'];

    function DivisionEventTableController ($scope, $state,DivisionEvent,DivisionEventTable) {
        var vm = this;
        
        vm.divisionEvents = [];
        vm.divisionEventEntity = DivisionEvent.get({id : $state.params.id});
    
        loadAll();

        function loadAll() {
        	DivisionEventTable.query($state.params,function(result) {
                vm.divisionEvents = result;
            });
        }
    }
})();
