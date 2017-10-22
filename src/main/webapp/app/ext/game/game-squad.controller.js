(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameSquadController', GameSquadController);

    GameSquadController.$inject = ['$scope', '$state', 'Game','TeamExt','GameExt'];

    function GameSquadController ($scope, $state,Game,TeamExt,GameExt) {
        var vm = this;

        vm.save = save;

        vm.game = null;
        // all player of this team
        vm.players =[];
        // all selected player select from vm.players
        vm.squadPlayers = [];
        // all selected and saved from database
        vm.existSquadPlayers = [];


        vm.formation = null;

        vm.formations = [{"value":"352","label":"3-5-2"},
                           {"value":"442","label":"4-4-2"},
                           {"value":"442D","label":"4-4-2 diamond"},
                           {"value":"4411","label":"4-4-1-1"},
                           {"value":"433","label":"4-3-3"},
                           {"value":"451","label":"4-5-1"},
                           {"value":"4231","label":"4-2-3-1"},
                           {"value":"532","label":"5-3-2"},
                           {"value":"4222","label":"4-2-2-2"},
                           {"value":"3412","label":"3-4-1-2"},
                           {"value":"343","label":"3-4-3"},
                           {"value":"3421","label":"3-4-2-1"},
                           {"value":"4141","label":"4-1-4-1"}
                          ];
         vm.postionMapping = ['GK','LB','LCB','CB','RCB','RB','LDM','CDM','RDM','LM','LCM','CM','RCM','RM','LAM','CAM','RAM','LF','CF','RF','SB1','SB2','SB3','SB4','SB5','SB6','SB7'];
         vm.positionFormationMapping = [',352,442,442D,4411,433,451,4231,532,4222,3412,343,3421,4141,',
                                            ',442,442D,4411,433,451,4231,532,4222,4141,',
                                            ',352,442,442D,4411,433,451,4231,532,4222,3412,343,3421,4141,',
                                            ',352,532,3412,343,3421,',
                                            ',352,442,442D,4411,433,451,4231,532,4222,3412,343,3421,4141,',
                                            ',442,442D,4411,433,451,4231,532,4222,4141,',
                                            ',4231,4222,',
                                            ',442D,4141,',
                                            ',4231,4222,',
                                            ',352,442,442D,4411,433,451,532,3412,343,3421,4141,',
                                            ',352,442,4411,451,3412,343,3421,4141,',
                                            ',352,433,451,532,',
                                            ',352,442,4411,451,3412,343,3421,4141,',
                                            ',352,442,442D,4411,433,451,532,3412,343,3421,4141,',
                                            ',4231,4222,3421,',
                                            ',442D,4411,4231,3412,',
                                            ',4231,4222,3421,',
                                            ',352,442,442D,433,532,4222,3412,343,',
                                            ',4411,433,451,4231,343,3421,4141,',
                                            ',352,442,442D,433,532,4222,3412,343,',
                                            '','','','','','',''];

        loadAll();

        function parseToPosition(playerNumber){
            if(!playerNumber){
              return null;
            }
            var number = parseInt(playerNumber);
            return vm.postionMapping[number-1];
        }


        function loadAll() {

        	Game.get({id:$state.params.id},function(result){
        		 vm.game = result;
               // set home team when $state is exist homeTeam or $state tid equals homeTeam 
        		 if($state.current.data.homeTeam || ($state.params.tid && $state.params.tid == vm.game.homeTeam.id)){
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
            	GameExt.queryGameSquadByTeam(data,function(result){
     				vm.existSquadPlayers = result;

              angular.forEach(vm.existSquadPlayers,function(squadPlayer,squadIndex){
                squadPlayer.playerPosition = parseToPosition(squadPlayer.playerNumber);
              });

     				angular.forEach(vm.players,function(player,index){
     					angular.forEach(vm.existSquadPlayers,function(squadPlayer,squadIndex){
     						if(player.id === squadPlayer.player.id){
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

            angular.forEach(angular.element('select,input:hidden'),function(item,index){
                var $item = angular.element(item);
                var val  = $item .val();
                var top  = $item .data('data-top');
                var left = $item .data('data-left');
                if (top && left) {
                    val += ',' + top + ',' + left;

                    $item .val(val);
                }
                // console.log(val);
            });

            angular.forEach(angular.element('#selectors input:hidden'),function(item,index){
               var name = item.name;
               var value = item.value;
               if(value){
                    var temp = {'player':{'id':parseInt(value)}};
                    if(name.indexOf('SB')!=-1){
                        temp.isSubstitute = true;
                    }else{
                        temp.isSubstitute = false;
                    }

                    for (var i = 0; i < vm.postionMapping.length; i++) {
                      if(vm.postionMapping[i] == name){
                          temp.playerNumber = i +1;
                          break;
                      }
                    };

                    vm.squadPlayers.push(temp);
               }

            });


            // console.log(vm.formation);
            // console.log(vm.squadPlayers);

            vm.data = {
            		players:vm.squadPlayers,
            		id:$state.params.id,
            		team:vm.team.id
            };

            GameExt.saveGameSquad(vm.data,onSaveSuccess,onSaveError);
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
