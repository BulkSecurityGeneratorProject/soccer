(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubDashboardController', ClubDashboardController);

    ClubDashboardController.$inject = ['$scope','$state','Club','ClubExt','TeamExt'];

    function ClubDashboardController ($scope, $state,Club,ClubExt,TeamExt) {
        var vm = this;
        
        $scope.now = new Date();
        $scope.isBefore = function(dStr) {
        		return $scope.now.getTime() > new Date(dStr).getTime();
        }
        
        // 引用其他的Service组合成Dashboard
        vm.club = Club.get({id : $state.params.id});
        // Schedule
        vm.games = ClubExt.queryGames({id : $state.params.id});
        
        // Next game
        ClubExt.queryNextGame({id : $state.params.id},function(result){
        	 vm.nextGame = result;
        	 if(vm.nextGame && vm.nextGame.homeTeam && vm.nextGame.roadTeam){
        		 if(vm.nextGame.homeTeam.club == $state.params.id){
            		 vm.nextTeam = vm.nextGame.roadTeam;
            	 }else{
            		 vm.nextTeam = vm.nextGame.homeTeam;
            	 }
        		 // get recent games of next team
        		 vm.recentGamesOfNextTeam = TeamExt.queryPassedGames({id : vm.nextTeam.id,count:5});
        	 }
        	
        });
        
    }
})();
