(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameSquadController', GameSquadController);

    GameSquadController.$inject = ['$scope', '$state','Game','TeamPlayer'];

    function GameSquadController ($scope, $state,Game,TeamPlayer) {
        var vm = this;
        
        vm.game = null;
        vm.players =[];
        
        loadAll();

        function loadAll() {
//            Game.query(function(result) {
//                vm.games = result;
//            });
        	Game.get({id:$state.params.id},function(result){
        		 vm.game = result;
        		 if($state.current.data.homeTeam){
        			 vm.team = vm.game.homeTeam;
        		 }else{
        			 vm.team = vm.game.roadTeam;
        		 }
        		 // load all player of team
     			TeamPlayer.query({id:vm.team.id},function(result){
     				vm.players = result;
     			});
        	});
        }
    }
})();
