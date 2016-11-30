(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicDivisionController', PublicDivisionController);

    PublicDivisionController.$inject = ['$scope','$state','Division','DivisionExt','DivisionEvent','DivisionEventTable','DivisionEventGoalRanking','DivisionEventAssistRanking','DivisionEventGame'];

    function PublicDivisionController ($scope, $state,Division,DivisionExt,DivisionEvent,DivisionEventTable,DivisionEventGoalRanking,DivisionEventAssistRanking,DivisionEventGame) {
        var vm = this;
        $scope.now = new Date();
        $scope.tabs = [{
            title: 'Overview',
            url: 'overview.tpl.html'
        },{
            title: 'Schduled',
            url: 'schduled.tpl.html'
        },{
            title: 'Clubs',
            url: 'clubs.tpl.html'
        }, {
            title: 'Players',
            url: 'players.tpl.html'
        }, {
            title: 'Venues',
            url: 'venues.tpl.html'
        }];
        
        $scope.currentTab = 'overview.tpl.html';

        $scope.onClickTab = function (tab) {
            $scope.currentTab = tab.url;
        }
        
        $scope.isActiveTab = function(tabUrl) {
            return tabUrl == $scope.currentTab;
        }
        $scope.now = new Date();
        $scope.isBefore = function(dStr) {
            return $scope.now.getTime() > new Date(dStr).getTime();
        }
        
        /**
         * Change division handle
         */
        $scope.changeToDivisionEvent = function(divisionEvent){
        	$scope.currentDivisionEvent = divisionEvent;
        	refreshPage();
        }
        
        function recentMonth(obj) {
        	return	 Math.abs(($scope.now.getTime() - new Date(obj.startAt).getTime())/(24 * 60 * 60 * 1000)) <=31;
        }
        
        // 1. division basic information
        vm.division = Division.get({id:$state.params.id});
        // 1.1 get all division events order by season
        DivisionExt.queryDivisions({id:$state.params.id},function(result){
        	vm.divisionEvents = result;
        	// initialize division event
        	$scope.changeToDivisionEvent(vm.divisionEvents[0]);
        });
        
        function refreshPage(){
       	 // all below is require a event id,and event id is get from division and season
       	 // 2. schduled information
       	var id = $scope.currentDivisionEvent.id;
           DivisionEventGame.query({id:id},function(result){
           	 vm.games = result;
           	 vm.recentGames = vm.games.filter(recentMonth);
           });
           
           // 3. Clubs table(statistics)
           vm.divisionTable = DivisionEventTable.query({id:id});
           // 4. Player table(statistics)
           vm.goalRankings = DivisionEventGoalRanking.query({id:id});
           vm.assistRankings = DivisionEventAssistRanking.query({id:id});
           // 5. venus table
       }
    }
})();
