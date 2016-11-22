(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicClubController', PublicClubController)
        .filter('ageFilter', function() {
            function calculateAge(birthday) { // birthday is a date
                var ageDifMs = Date.now() - birthday.getTime();
                var ageDate = new Date(ageDifMs); // miliseconds from epoch
                return Math.abs(ageDate.getUTCFullYear() - 1970);
            }
            return function(birthdate) {
                  return calculateAge(new Date(birthdate));
            }; 
       });

    PublicClubController.$inject = ['$scope','$state',  'Club','TeamPlayer','TeamGame'];

    function PublicClubController ($scope, $state,Club,TeamPlayer,TeamGame) {
    	var clubId = 1;
    	
        var vm = this;
        $scope.tabs = [{
	            title: 'Overview',
	            url: 'overview.tpl.html'
	        }, {
	            title: 'Players',
	            url: 'players.tpl.html'
	        }, {
	            title: 'Venue',
	            url: 'venue.tpl.html'
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
        
        
        
        // 1. Get Club Basic information
        // vm.club = Club.Get({id : $state.params.id});
        vm.club = Club.get({id:clubId});
        // 2. Get All Club team with player
        vm.players = TeamPlayer.query({id:1});
        // 3. Get schdule of teams
       TeamGame.query({id:1},function(result){
    	   vm.games = result;
        });
        // 4. History and so on
        
    }
})();
