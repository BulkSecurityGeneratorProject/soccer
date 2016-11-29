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

    DivisionEventGameController.$inject = ['$scope', '$state', 'DivisionEventGame','DivisionEventTeam','Game'];

    function DivisionEventGameController ($scope, $state,DivisionEventGame,DivisionEventTeam,Game) {
        var vm = this;
        vm.save = saveRow;
        vm.deleteRow = deleteRow;
        vm.openCalendar = openCalendar;
        vm.datePickerOpenStatus = {};
        vm.generateGames = generateGames;
        vm.generate = {}; // 生成比赛实例
        vm.generate.every = {};
        vm.generate.every.replies =[{key: 0, value: ""}];
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
        
        function getDateBetween(startAt,endAt,day,interval,hour,minute,dates){
        	var startAtDate =new Date();
        	startAtDate.setTime(startAt.getTime());
        	var endAtDate = new Date();
        	endAtDate.setTime(endAt.getTime());
        	
        	while(startAtDate < endAtDate){
        		var lastest = (7+ day - startAtDate.getDay())%7;
        		if(lastest ==0){
        			startAtDate.setMinutes(parseInt(vm.minutesOfHour[minute].name));
        			if(interval ==0){
        				// 上午
        				startAtDate.setHours(parseInt(vm.hoursOfDay[hour].name));
        			}else{
        				// 下午
        				startAtDate.setHours(12+ parseInt(vm.hoursOfDay[hour].name));
        			}
        			var okDate = new Date();
        			okDate.setTime(startAtDate.getTime());
        			dates.push(okDate);
        			startAtDate.setDate(startAtDate.getDate()+7);
        		}else{
        			startAtDate.setDate(startAtDate.getDate()+lastest);
        		}
        	}
        }
        
        function generateGames(){
        	// get all args
            // console.log("=========start At "+ vm.generate.startAt+"  endAt "+vm.generate.endAt + "  every "+vm.generate.every.day+" "+vm.generate.every.interval+" "+ vm.generate.every.hour+" "+vm.generate.every.minute);
        	var startAt = vm.generate.startAt;
        	var endAt = vm.generate.endAt;
        	var everyData = vm.generate.every.data;
        	var i = 0;
        	
        	var dates = new Array();
        	while(everyData.hasOwnProperty(i)){
        		var data = everyData[i];
        		var day = data.day;
        		var interval = data.interval;
        		var hour = data.hour;
        		var minute = data.minute;
        		getDateBetween(startAt,endAt,day,interval,hour,minute,dates);
        		i++;
        	}
        	
        	// 日期从小到大排序
        	vm.dates =  dates.sort(function(a,b){
        		return a.getTime() - b.getTime();
        	});
        	// compute min-max timeslot
        	DivisionEventTeam.query($state.params,function(result){
        		vm.teams = result;
        		vm.minTimeslot = (vm.teams.length -1)*2; // 主客场各一轮
        		vm.maxTimeslot = vm.minTimeslot; // maxTimeslot not supported
        		
        		// 排列组合 https://en.wikipedia.org/wiki/Round-robin_tournament
        		// 偶数个队，保证每个队都能完成（队数-1）*2次比赛，保证主客场数量一致，保证最多且最少与另一队进行一次主、客场比赛
        		// 奇数个队，保证每个队都能完成（队数-1）*2次比赛，保证主客场数量一致
        		 vm.rr = createSchedule();
        		 vm.newGames = parsetToGame(vm.rr);
        		 vm.tempGamesConfig = generateConfig(vm.newGames);
        	});
        	
        }
        
        function parsetToGame(ary){
        	var result = [];
        	for(var i=0;i<ary.length;i++){
        		// rounds
        		var round = i+1;
        		for(var j=0;j<ary[i].length;j++){
        			// games
        			var g = ary[i][j];
        			var venueId = '';
        			var venueName='';
        			if(g[0].club && g[0].club.venue){
        				venueId = g[0].club.venue.id;
        				venueName = g[0].club.venue.name;
        			}
        			
        			var timeslot =  vm.dates.length;
        			var matchesOfTimeslot = Math.ceil(ary.length*ary[0].length / timeslot);
        			// var matchesLeft = ary.length % timeslot;
        			var time = new Date();
        			var whichTimeslotDate = Math.floor((i*ary[0].length+j)/matchesOfTimeslot);
        			
        			// console.log(" 每轮应该进行多少场比赛： "+matchesOfTimeslot+" 当前比赛在哪个比赛日："+whichTimeslotDate);
        			if(whichTimeslotDate < timeslot){
        				// 取余分布
        				time.setTime(vm.dates[whichTimeslotDate].getTime());
        			}else{ 
        				// 余数比赛,因为是最后的比赛,所以简单的放到最后一天进行,不合理的地方,需要由人工进行干预
        				time.setTime(vm.dates[timeslot-1].getTime());
        			}
        			
        			var game = {
        					id:"",
        					round:round,
        					time:time.getFullYear()+"-"+(time.getMonth()+1)+"-"+time.getDate()+" "+time.getHours()+":"+time.getMinutes(),
        					timeslotTypeName:"",
        					homeTeamName:g[0].name,
        					roadTeamName:g[1].name,
        					venueName:venueName,
        					venue:{
        						id:venueId
        					},
        					homeTeam:{
        						id:g[0].id,
        						name:g[0].name
        					},
        					roadTeam:{
        						id:g[1].id,
        						name:g[1].name
        					}
        			};
        			result.push(game);
        		}
        	}
        	return result;
        }
        
        function cut(array,begin,end){
        	var result = [];
        	for(var i =0,j=begin;i<end;i++,j++){
        		result[i] = array[begin];
        	}
        }
        
        function createSchedule(){
        		 var teams = vm.teams.length;
        	     var  i;
        	     //var  ret = "" ;
        	     var  round;
        	     var  numplayers = 0;
        	     numplayers = parseInt(teams) + parseInt(teams % 2);
        	     numplayers = parseInt(numplayers);
        	     var  a = new  Array(numplayers - 1);
        	     var map = {};
        	     var  alength = a.length;
        	     for  (var  x = 0; x < (numplayers); x++) { a[x] = vm.teams[x]; }
        	     if  (numplayers != parseInt(teams)) { a[alength] = "BYE" ; }
        	     var  pos;
        	     var  pos2;
        	     var roundGames = new Array(vm.minTimeslot);
        	     
        	     //ret = "----- ROUND #1-----<br />" 
        	     var gameArrays =  new Array(numplayers / 2);
        	     
        	     for  (var  r1a = 0; r1a < (numplayers / 2); r1a++) {
        	       //ret += a[r1a] + " vs. "  + a[alength - r1a] + "<br />" 
        	       gameArrays[r1a] = [a[r1a],a[alength - r1a]];
        	     }
        	     roundGames[0] = gameArrays;
        	     //赛季上半程
        	     for  (round = 2; round < alength + 1; round++) {
        	    	 var gameArrays =  new Array(numplayers / 2);
        	    	 
        	         //ret += "<br /><br />----- ROUND #"  + round + "-----<br />" 
        	         // 随机保证主客场
        	         if(Math.random()*10 >5){
        	        	 //ret +=   a[alength - (round - 1)]+ " vs. "  + a[0]+ "<br />" 
        	        	 gameArrays[0] = [a[alength - (round - 1)],a[0]];
        	         }else{
        	        	 //ret += a[0] + " vs. "  + a[alength - (round - 1)] + "<br />" 
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
        	             //ret += a[(alength + pos2)]
        	             //ret += " vs. "  + a[(alength - pos)] + "<br />" 
        	             gameArrays[i-1] = [a[(alength + pos2)],a[(alength - pos)]];
        	         }
        	         roundGames[round-1] = gameArrays;
        	     }
        	     // 下半程
        	     for  (var r = alength+1; r < alength*2+1; r++) {
        	    	 //ret += "<br /><br />----- ROUND #"+r+"-----<br />";
        	    	 var gameArrays =  new Array(numplayers / 2);
        	    	 for(var  i = 0; i < (numplayers / 2); i++){
        	    		 //主客场调换
        	    		 //ret += roundGames[r-alength-1][i][1] + " vs. "  + roundGames[r-alength-1][i][0] + "<br />" 
        	    		 gameArrays[i] =  [roundGames[r-alength-1][i][1],roundGames[r-alength-1][i][0]];
        	    	 }
        	    	 roundGames[r-1] = gameArrays;
        	     }
        	     //return  ret;
        	     return roundGames;
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
                vm.gamesConfig = generateConfig(vm.games);
            });
        }
        
        function generateConfig(data){
        	// timeslot + game
        	var columns = [
				{field:'id',disabled:true,hidden:true},
				{field: 'round',"title": "轮次",inputType: 'number'},
				{field:'timeslotTypeName','title':'类别'},
				{field:'time',title:'比赛时间',inputType: "datetime",},
				{field:'homeTeamName',title:'主队'},
				{field:'roadTeamName',title:'客队'},
				{field:'venueName',title:'场地'}
			];
			
        	var result = {
	        	    getData: function () { return data; }, 
	        	    options: { 
	        	        showDeleteButton: true,
	        	        showEditButton: true,
	        	        editable: true,
	        	        disabled:false,
	        	        perRowEditModeEnabled: true,
	        	        allowMultiSelect: false,
	        	        dynamicColumns: true,
	        	        /*editRequested:vm.save,*/
	        	        rowDeleted:vm.deleteRow,
	        	        columns: columns,
	        	        saveAll:vm.saveAll
	        	    }
	        	};
        	return result;
        }
        
        /**
         * 已失效，不可使用
         */
        function saveRow(row){
        	if(!row.$editable){ // 提交状态
	       		vm.isSaving = true;
	       		console.log(row);
	       		var game = new Object();
	       		game.id = row.id;
	       		game.startAt = new Date(Date.parse(row.time,'yyyy-MM-dd HH:mm'));
	       		game.venue = new Object();
	       		game.venue.id = row.venue.id;
	       		game.homeTeam = new Object();
	       		game.homeTeam.id = row.homeTeam.id;
	       		game.roadTeam = new Object();
	       		game.roadTeam.id = row.roadTeam.id;
	       		game.timeslot = new Object();
	       		game.timeslot.round = row.round;
	       		game.timeslot.divisionEvent = new Object();
	       		game.timeslot.divisionEvent.id = $state.params.id;
	       		DivisionEventGame.save($state.params,game,onSaveSuccess,onSaveError);
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
        
        vm.saveAll = function(data){
        	vm.isSaving = true;
        	var games = data.map(function(row){
        		var game = new Object();
           		game.id = row.id;
           		game.startAt = new Date(Date.parse(row.time,'yyyy-MM-dd HH:mm'));
           		game.venue = new Object();
           		game.venue.id = row.venue.id;
           		game.homeTeam = new Object();
           		game.homeTeam.id = row.homeTeam.id;
           		game.roadTeam = new Object();
           		game.roadTeam.id = row.roadTeam.id;
           		game.timeslot = new Object();
           		game.timeslot.round = row.round;
           		game.timeslot.divisionEvent = new Object();
           		game.timeslot.divisionEvent.id = $state.params.id;
           		return game;
        	});
       		
       		DivisionEventGame.saveBatch($state.params,games,onSaveSuccess,onSaveError);
        }
        
        vm.incrEvery = function(idx){
        	vm.generate.every.replies.splice(idx + 1, 0,
        			{key: new Date().getTime(), value: ""});   // 用时间戳作为每个item的key
        			// 增加新的回复后允许删除
        			vm.generate.every.canDescReply = true;
        };
        
        vm.decrEvery = function(idx) {
        	// 如果回复数大于1，删除被点击回复
        	if (vm.generate.every.replies.length > 1) {
        		vm.generate.every.replies.splice(idx, 1);
        	}
           // 如果回复数为1，不允许删除
        	 if (vm.generate.every.replies.length == 1) {
        		 vm.generate.every.canDescReply = false;
        	 }
         };
         
         vm.combineReplies = function() {
	        var cr = "";
	        for (var i = 0; i < vm.generate.every.replies.length; i++) {
	        	cr += "#" + vm.generate.every.replies[i].value;
	        }
	        cr = cr.substring(1);
	        //$log.debug("Combined replies: " + cr);
	        return cr;
         }
    }
})();
