(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventGameController', DivisionEventGameController)
        .filter('htmlContent',['$sce', function($sce) {
			return function(input) {
				return $sce.trustAsHtml(input);
			}
		}]);

    DivisionEventGameController.$inject = ['$scope', '$state', 'DivisionEventGame','DivisionEventTeam'];

    function DivisionEventGameController ($scope, $state,DivisionEventGame,DivisionEventTeam) {
        var vm = this;
        vm.save = saveRow;
        vm.deleteRow = deleteRow;
        vm.openCalendar = openCalendar;
        vm.datePickerOpenStatus = {};
        vm.generateGames = generateGames;
        vm.generate = {}; // 生成比赛实例
        vm.teams = [];
        vm.daysOfWeek = [
                         {id:0,name:'周日'},
                         {id:1,name:'周一'},
                         {id:2,name:'周二'},
                         {id:3,name:'周三'},
                         {id:4,name:'周四'},
                         {id:5,name:'周五'},
                         {id:6,name:'周六'}
        ];
        vm.hoursOfDay = [
                         {id:0,name:'1'},
                         {id:1,name:'2'},
                         {id:2,name:'3'},
                         {id:3,name:'4'},
                         {id:4,name:'5'},
                         {id:5,name:'6'},
                         {id:6,name:'7'},
                         {id:7,name:'8'},
                         {id:8,name:'9'},
                         {id:9,name:'10'},
                         {id:10,name:'11'},
                         {id:11,name:'12'}
                         ];
        vm.minutesOfHour = [
                         {id:0,name:'00'},
                         {id:1,name:'05'},
                         {id:2,name:'10'},
                         {id:3,name:'15'},
                         {id:4,name:'20'},
                         {id:5,name:'25'},
                         {id:6,name:'30'},
                         {id:7,name:'35'},
                         {id:8,name:'40'},
                         {id:9,name:'45'},
                         {id:10,name:'50'},
                         {id:11,name:'55'}
                         ];
        
        vm.intervalOfTime = [{id:0,name:'上午'},{id:1,name:'下午'}];
        
        vm.games = [];
        vm.gamesConfig = null;

        loadAll();
        
        function generateGames(){
        	// get all args
            // console.log("=========start At "+ vm.generate.startAt+"  endAt "+vm.generate.endAt + "  every "+vm.generate.every.day+" "+vm.generate.every.interval+" "+ vm.generate.every.hour+" "+vm.generate.every.minute);
        	
        	// compute min-max timeslot
        	DivisionEventTeam.query($state.params,function(result){
        		vm.teams = result;
        		vm.minTimeslot = (vm.teams.length -1)*2; // 主客场各一轮
        		vm.maxTimeslot = vm.minTimeslot; // maxTimeslot not supported
        		
        		// 排列组合 https://en.wikipedia.org/wiki/Round-robin_tournament
        		// 偶数个队，保证每个队都能完成（队数-1）*2次比赛，保证主客场数量一致，保证最多且最少与另一队进行一次主、客场比赛
        		// 奇数个队，保证每个队都能完成（队数-1）*2次比赛，保证主客场数量一致
        		 vm.rr = createSchedule();
        	});
        	
        }
       //vm.rr = RoundRobin(11);
        
        function cut(array,begin,end){
        	var result = [];
        	for(var i =0,j=begin;i<end;i++,j++){
        		result[i] = array[begin];
        	}
        }
        
        function createSchedule(){
        		 var teams = vm.teams.length;
        	     var  i;
        	     var  ret = "" ;
        	     var  round;
        	     var  numplayers = 0;
        	     numplayers = parseInt(teams) + parseInt(teams % 2);
        	     numplayers = parseInt(numplayers);
        	     var  a = new  Array(numplayers - 1);
        	     var map = {};
        	     var  alength = a.length;
        	     for  (var  x = 0; x < (numplayers); x++) { a[x] = vm.teams[x].name; }
        	     if  (numplayers != parseInt(teams)) { a[alength] = "BYE" ; }
        	     var  pos;
        	     var  pos2;
        	     var roundGames = new Array(vm.minTimeslot);
        	     
        	     ret = "----- ROUND #1-----<br />" 
        	     var gameArrays =  new Array(numplayers / 2);
        	     
        	     for  (var  r1a = 0; r1a < (numplayers / 2); r1a++) {
        	       ret += a[r1a] + " vs. "  + a[alength - r1a] + "<br />" 
        	       gameArrays[r1a] = [a[r1a],a[alength - r1a]];
        	     }
        	     roundGames[0] = gameArrays;
        	     //赛季上半程
        	     for  (round = 2; round < alength + 1; round++) {
        	    	 var gameArrays =  new Array(numplayers / 2);
        	    	 
        	         ret += "<br /><br />----- ROUND #"  + round + "-----<br />" 
        	         // 随机保证主客场
        	         if(Math.random()*10 >5){
        	        	 ret +=   a[alength - (round - 1)]+ " vs. "  + a[0]+ "<br />" 
        	        	 gameArrays[0] = [a[alength - (round - 1)],a[0]];
        	         }else{
        	        	 ret += a[0] + " vs. "  + a[alength - (round - 1)] + "<br />" 
        	        	 gameArrays[0] = [a[0],a[alength - (round - 1)]];
        	         }
        	         
        	         for  (i = 2; i < (numplayers / 2) + 1; i++) {
        	             pos = (i + (round - 2))
        	             if  (pos >= alength) { pos = ((alength - pos)) * -1 }
        	             else 
        	             { pos = (i + (round - 2)) }
        	 
        	             pos2 = (pos - (round - 2)) - round
        	             if  (pos2 > 0) {
        	                 pos2 = (alength - pos2) * -1
        	             }
        	 
        	             if  (pos2 < (alength * -1)) {
        	                 pos2 += alength
        	             }
        	             ret += a[(alength + pos2)]
        	             ret += " vs. "  + a[(alength - pos)] + "<br />" 
        	             gameArrays[i-1] = [a[(alength + pos2)],a[(alength - pos)]];
        	         }
        	         roundGames[round-1] = gameArrays;
        	     }
        	     // 下半程
        	     for  (var r = alength+1; r < alength*2+1; r++) {
        	    	 ret += "<br /><br />----- ROUND #"+r+"-----<br />";
        	    	 for(var  i = 0; i < (numplayers / 2); i++){
        	    		 //主客场调换
        	    		 ret += roundGames[r-alength-1][i][1] + " vs. "  + roundGames[r-alength-1][i][0] + "<br />" 
        	    	 }
        	     }
        	     return  ret;
        	 }
        
        function loadAll() {
        	DivisionEventGame.query($state.params,function(result) {
                vm.games = result;
                angular.forEach(vm.games,function(game,index){
	            	if(game.timeslot && game.timeslot.type){
	            		game.timeslotTypeName = game.timeslot.type.name;
	            	}else{
	            		game.timeslotTypeName = '';
	            	}
	            	if(game.timeslot){
	            		game.round = game.timeslot.round;
	            	}
	            	game.time = game.startAt;
	            	game.homeTeamName = game.homeTeam.name;
	            	game.roadTeamName = game.roadTeam.name;
	            	game.venueName = game.venue.name;
	            	
                });
                vm.gamesConfig = generateConfig();
            });
        }
        
        function generateConfig(){
        	// timeslot + game
        	var columns = [
				{field:'id',disabled:true,hidden:true},
				{field: 'round',"title": "轮次",inputType: 'number'},
				{field:'timeslotTypeName','title':'类别'},
				{field:'time',title:'比赛时间'},
				{field:'homeTeamName',title:'主队'},
				{field:'roadTeamName',title:'客队'},
				{field:'venueName',title:'场地'}
			];
			
        	var result = {
	        	    getData: function () { return vm.games; }, 
	        	    options: { 
	        	        showDeleteButton: true,
	        	        showEditButton: true,
	        	        editable: true,
	        	        disabled:false,
	        	        perRowEditModeEnabled: true,
	        	        allowMultiSelect: false,
	        	        dynamicColumns: true,
	        	        editRequested:vm.save,
	        	        rowDeleted:vm.deleteRow,
	        	        columns: columns
	        	    }
	        	};
        	return result;
        }
        
        function saveRow(row){
        	if(!row.$editable){ // 提交状态
	       		vm.isSaving = true;
	       		DivisionEventGame.save($state.params,row,onSaveSuccess,onSaveError);
        	}
        }
        
        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:divisionEventGamesUpdate', result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }
        
        function deleteRow(row){
        	if(!row.$editable){ // 提交状态
        		vm.isDeleting = true;
        		Game.delete({id:row.id},onDeleteSuccess,onDeleteError);
        		// refresh data
        		vm.games.splice(vm.games.indexOf(row),1);
        	}
        }
        function onDeleteSuccess(result){
        	$scope.$emit('soccerApp:divisionEventGamesDelete', result);
        	 vm.isDeleting = false;
        }
        function onDeleteError(){
        	vm.isDeleting = false;
        }
        
        vm.datePickerOpenStatus.startAt = false;
        vm.datePickerOpenStatus.endAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
