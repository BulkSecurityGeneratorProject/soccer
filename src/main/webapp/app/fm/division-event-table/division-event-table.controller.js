(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventTableController', DivisionEventTableController);

    DivisionEventTableController.$inject = ['$scope','$state',  'DivisionEvent', 'DivisionEventTable','DivisionEventGoalRanking','DivisionEventAssistRanking'];

    function DivisionEventTableController ($scope, $state,DivisionEvent,DivisionEventTable,DivisionEventGoalRanking,DivisionEventAssistRanking) {
        var vm = this;
        
        vm.divisionEvents = [];
        vm.divisionEventEntity = DivisionEvent.get({id : $state.params.id});
    
        loadAll();

        function loadAll() {
        	DivisionEventTable.query($state.params,function(result) {
                vm.divisionEvents = result;
            });
        	DivisionEventGoalRanking.query($state.params,function(result){
        		vm.goalRankings = result;
        	});
        	DivisionEventAssistRanking.query($state.params,function(result){
        		vm.assistRankings = result;
        	});
        }
    }
})();
