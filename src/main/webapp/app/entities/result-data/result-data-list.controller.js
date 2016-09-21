(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultDataListEditController', ResultDataListEditController);

    ResultDataListEditController.$inject = ['$scope','$state','Game','GameSquadQuery','ResultField','ResultDataSquad'];

    function ResultDataListEditController($scope,$state,Game,GameSquadQuery,ResultField,ResultDataSquad) {
        var vm = this;

        // 1. Get game information
         Game.get({id:$state.params.id},function(result){
        	 vm.game = result;
        	 
        	// 2. Get players information
             var data = {
         			id:$state.params.id,
         			tid: vm.game.homeTeam.id
         	};
             GameSquadQuery.query(data,function(result){
     				vm.players = result;
     				// Get all exists result data of this squad
     				ResultDataSquad.query({id:vm.players[0].squad.id},function(result){
     					vm.resultDatas = result;
     					// translate
         				angular.forEach(vm.players,function(player,index){
         					player.playerName = player.player.name;
         					
         					angular.forEach(vm.resultDatas,function(resultData,index){
         						if(player.player.id == resultData.squadPlayer.player.id){
             						player[""+resultData.resultField.id] = resultData.value;
             					}
         					});
         					
         				});
         				
         				// 3. Get all result field
         				ResultField.query(function(result){
         					vm.resultFields = result;
         					
         					var columns = [
    								{field:'id',disabled:true,hidden:true},
    								{field: 'playerName', disabled: true,"title": "姓名"},
    								{field:'playerNumber',inputType: 'number','title':'号码'},
    								{field:'isSubstitute',inputType: 'checkbox','title':'替补'}
         					];
         					
         					angular.forEach(vm.resultFields,function(resultField,index){
         						columns = columns.concat([{field:resultField.id,title:resultField.name,inputType: 'number'}]);
         					});
         					
         					vm.myGridConfig = {
         			        	    // should return your data (an array)        
         			        	    getData: function () { return vm.players; }, 

         			        	    options: { 
         			        	        showDelete: true,
         			        	        columns: columns
         			        	    }
         			        	};
         					
         				});
         				
     				});
     				
     		});
         });
        
    }
})();
