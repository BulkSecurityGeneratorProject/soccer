(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicClubController', PublicClubController);

    PublicClubController.$inject = ['$scope','$state',  'Club','TeamPlayer'];

    function PublicClubController ($scope, $state,Club,TeamPlayer) {
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
        
        // 1. Get Club Basic information
        // vm.club = Club.Get({id : $state.params.id});
        vm.club = Club.get({id:clubId});
        // 2. Get All Club team with player
        vm.players = TeamPlayer.query({id:1});
        // 3. Get schdule of teams
        
        // 4. History and so on
    }
})();
