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
        vm.club = Club.get({id:$state.params.id});
        // 2. Get All Club team with player
        vm.players = TeamPlayer.query({id:$state.params.id});
        // 3. Get schdule of teams
        vm.games = TeamGame.query({id:$state.params.id});
        // 4. History and so on
        
    }
})();
