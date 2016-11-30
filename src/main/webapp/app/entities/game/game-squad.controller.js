(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameSquadController', GameSquadController);

    GameSquadController.$inject = ['$scope', '$state', 'Game','TeamExt','GameSquad','GameSquadQuery'];

    function GameSquadController ($scope, $state,Game,TeamExt,GameSquad,GameSquadQuery) {
        var vm = this;
        
        vm.save = save;
        
        vm.game = null;
        // all player of this team
        vm.players =[];
        // all selected player select from vm.players
        vm.squadPlayers = [];
        // all selected and saved from database
        vm.existSquadPlayers = [];
       
        loadAll();

        function loadAll() {
        	
        	Game.get({id:$state.params.id},function(result){
        		 vm.game = result;
        		 if($state.current.data.homeTeam){
        			 vm.team = vm.game.homeTeam;
        		 }else{
        			 vm.team = vm.game.roadTeam;
        		 }
        		 // load all player of team
        		 TeamExt.queryPlayers({id:vm.team.id},function(result){
     				vm.players = result;
     			});
     			
     			// load all exist squad players
            	var data = {
            			id:$state.params.id,
            			tid:vm.team.id
            	};
            	GameSquadQuery.query(data,function(result){
     				vm.existSquadPlayers = result;
     				angular.forEach(vm.players,function(player,index){
     					angular.forEach(vm.existSquadPlayers,function(squadPlayer,squadIndex){
     						if(player.id == squadPlayer.player.id){
     							// make player select
     							player.selected = true;
     						}
     					});
     				});
     				
     			});
        	});
        	
        }
       
        function save () {
            vm.isSaving = true;
           
            vm.data = {
            		players:vm.squadPlayers,
            		id:$state.params.id,
            		team:vm.team.id
            };
            
            GameSquad.save(vm.data,onSaveSuccess,onSaveError);
        }
        
        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:gameSquadUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
    }
})();
