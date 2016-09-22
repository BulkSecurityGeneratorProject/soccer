(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultDataListEditController', ResultDataListEditController);

    ResultDataListEditController.$inject = ['$scope','$state','Game','GameSquadQuery','ResultField','ResultDataSquad'];

    function ResultDataListEditController($scope,$state,Game,GameSquadQuery,ResultField,ResultDataSquad) {
        var vm = this;
        vm.save = saveRow;
        
        // 1. Get game information
         Game.get({id:$state.params.id},function(result){
        	 vm.game = result;
        	 
        	// 2. Get players information
             var data = {
         			id:$state.params.id,
         			tid: vm.game.homeTeam.id
         	};
             var roadData = {
          			id:$state.params.id,
          			tid: vm.game.roadTeam.id
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
             						player['$'+resultData.resultField.id] = resultData.id;
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
         			        	        showDeleteButton: false,
         			        	        showEditButton: true,
         			        	        editable: true,
         			        	        disabled:false,
         			        	        perRowEditModeEnabled: true,
         			        	        allowMultiSelect: false,
         			        	        dynamicColumns: true,
         			        	        editRequested:vm.save,
         			        	        columns: columns
         			        	    }
         			        	};
         					
         				});
         				
     				});
     				
     		});// End of home data load
             
             // road data load
             GameSquadQuery.query(roadData,function(result){
  				vm.roadPlayers = result;
  				// Get all exists result data of this squad
  				ResultDataSquad.query({id:vm.roadPlayers[0].squad.id},function(result){
  					vm.roadResultDatas = result;
  					// translate
      				angular.forEach(vm.roadPlayers,function(player,index){
      					player.playerName = player.player.name;
      					
      					angular.forEach(vm.roadResultDatas,function(resultData,index){
      						if(player.player.id == resultData.squadPlayer.player.id){
          						player[""+resultData.resultField.id] = resultData.value;
          						player['$'+resultData.resultField.id] = resultData.id;
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
      					
      					vm.myRoadGridConfig = {
      			        	    // should return your data (an array)        
      			        	    getData: function () { return vm.roadPlayers; }, 

      			        	    options: { 
      			        	        showDeleteButton: false,
      			        	        showEditButton: true,
      			        	        editable: true,
      			        	        disabled:false,
      			        	        perRowEditModeEnabled: true,
      			        	        allowMultiSelect: false,
      			        	        dynamicColumns: true,
      			        	        editRequested:vm.save,
      			        	        columns: columns
      			        	    }
      			        	};
      					
      				});
      				
  				});
  				
             });
         });
         
         function saveRow(row){
        	 if(!row.$editable){ // 提交状态
        		 //vm.isSaving = true;
        		 // translate row data to result data list
        		 
        		ResultDataSquad.save({id:vm.players[0].squad.id},parsetRowToResultDataList(row),onSaveSuccess,onSaveError);
        	 }
         }
         
         function parsetRowToResultDataList(row){
        	 var result = [];
        	 var squadPlayerId = row.id;
        	 // example construct : {id:xx,value:xx,squadPlayer : { },resultField: {id : 1} }
        	 angular.forEach(vm.resultFields,function(resultField,index){
        		 if(row[resultField.id]){// make sure that has a valid value
        			 var resultData = {};
        			 resultData.value = row[resultField.id];
        			 
        			 // result data record id
            		 if(row['$'+resultField.id]){ 
            			 resultData.id = row['$'+resultField.id];
            		 }
            		 resultData.resultField = {
            				 id:resultField.id
            		 };
            		 resultData.squadPlayer = {
            				 id:squadPlayerId
            		 };
            		 resultData.game = {
            				 id:$state.params.id
            		 };
            		 result = result.concat(resultData);
        		 }
        		 
        	 });
        	 return result;
         }
         
         function onSaveSuccess (result) {
             $scope.$emit('soccerApp:squadResultDataUpdate', result);
             vm.isSaving = false;
         }

         function onSaveError () {
             vm.isSaving = false;
         }
        
    }
})();
