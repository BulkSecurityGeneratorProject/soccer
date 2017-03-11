(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubDivisionEventSignupController', ClubDivisionEventSignupController);

    ClubDivisionEventSignupController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DivisionEvent', 'DictKindExt', 'TeamExt', 'Player','LineupExt'];

    function ClubDivisionEventSignupController($scope, $rootScope, $stateParams, previousState, entity, DivisionEvent, DictKindExt, TeamExt, Player,LineupExt) {
        var vm = this;

        vm.divisionEvent = entity;
        vm.previousState = previousState.name;
        vm.currentTeam = null;
        vm.currentPlayers=[];
       
        vm.positions = DictKindExt.queryDicts({id:'5'}); // position dicts
        
    	TeamExt.getByClubAndDivisionEvent({cid:$stateParams.id,did:$stateParams.did},function(result){
    		vm.currentTeam = result;
    		
    		TeamExt.queryPlayers({id:vm.currentTeam.id},function(result){
        		vm.currentPlayers = result;
        		
        		LineupExt.queryByDivisionEventAndTeam({did : $stateParams.did,tid : vm.currentTeam.id},function(result){
        			angular.forEach(result,function(lineup){
        				angular.forEach(vm.currentPlayers,function(player){
        					if(player.id == lineup.player.id){
        						player['playerNumber'] = lineup.playerNumber;
        						player['playerPosition'] = lineup.playerPosition.name;
        					}
        				});
        				
    				});
        			
        			generatGridConfig(vm.currentPlayers,function(result){
                   	 vm.myGridConfig = result;
                    });
        			
        		});
            	
        	});
    		
    	});
    	
    	
        
        vm.saveAll = function(data){
	       	var targetData = data.map(function(d){
	       		if(d.playerPosition && d.playerNumber){
	       			// Player number and player position is requried for lineup
	       			var obj = {};
		       		obj.player = {};
		       		obj.team = {};
		       		obj.divisionEvent = {};
		       		obj.playerPosition = {};
		       		
		       		obj.player.id = d.id;
		       		obj.team.id = d.team.id;
		       		obj.divisionEvent.id = d.divisionEvent.id;
		       		obj.playerNumber = d.playerNumber;
		       		obj.playerPosition.id = d.playerPosition.id;
		       		return obj;
	       		}
	       	}).filter(function(d){
	       		return d;
	       	});
	       	
	       	// 大名单最小18人，最大30人
	       	if(targetData.length>18 && targetData.length<=30){
	    	   // save lineup
	    	   LineupExt.saveBatch(targetData,onSaveSuccess,onSaveError);
	       }else{
	    	   $scope.errorMsg = "lineup min 18,max 30 player.";
	       }
       }
        
        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:lineupBatchUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError() {
            vm.isSaving = false;
        }
        
        function generatGridConfig(players,callback){
        	var options = vm.positions.map(function(obj){
        		var rObj = {};
        		rObj.title = obj.name;
        		rObj.value = obj;
        		return rObj;
        	});
    	    var columns = [
				{field:'id',disabled:true,hidden:true},
				{field: 'name', disabled: true,"title": "姓名"},
				{field:'playerNumber',inputType: 'number','title':'号码'},
				{field:'playerPosition',inputType:'select',title:'位置',options:options}
			];
			
			var result = {
	        	    getData: function () { return players;}, 
	        	    options: { 
	        	        showDeleteButton: false,
	        	        showEditButton: true,
	        	        editable: true,
	        	        disabled:false,
	        	        perRowEditModeEnabled: true,
	        	        allowMultiSelect: false,
	        	        dynamicColumns: true,
	        	        /*editRequested:vm.saveRow,*/
	        	        columns: columns,
	        	        saveAll:vm.saveAll
	        	    }
	        	};
				callback(result);
        }
        
    }
})();
