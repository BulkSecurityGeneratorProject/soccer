(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicDivisionController', PublicDivisionController);

    PublicDivisionController.$inject = ['$scope','$state','DivisionEvent','DivisionEventTable','DivisionEventGoalRanking','DivisionEventAssistRanking','DivisionEventGame'];

    function PublicDivisionController ($scope, $state,DivisionEvent,DivisionEventTable,DivisionEventGoalRanking,DivisionEventAssistRanking,DivisionEventGame) {
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
        
        // 1. division event basic information
        vm.division = DivisionEvent.get({id:$state.params.id});
        // 2. schduled information
        vm.games = DivisionEventGame.query({id:$state.params.id});
        // 3. Clubs table(statistics)
        vm.divisionTable = DivisionEventTable.query($state.params);
        // 4. Player table(statistics)
        vm.goalRankings = DivisionEventGoalRanking.query($state.params);
        vm.assistRankings = DivisionEventAssistRanking.query($state.params);
        // 5. venus table
    }
})();
