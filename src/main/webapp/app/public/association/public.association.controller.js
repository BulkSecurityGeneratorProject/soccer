(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicAssociationController', PublicAssociationController);

    PublicAssociationController.$inject = ['$scope','$state','Association','AssociationExt'];

    function PublicAssociationController ($scope, $state,Association,AssociationExt) {
        var vm = this;
        $scope.now = new Date();
        $scope.tabs = [{
            title: 'Overview',
            url: 'overview.tpl.html'
        },{
            title: 'Schduled',
            url: 'schduled.tpl.html'
        },{
            title: 'Divisions',
            url: 'divisions.tpl.html'
        },{
            title: 'Clubs',
            url: 'clubs.tpl.html'
        }, {
            title: 'Players',
            url: 'players.tpl.html'
        }, {
            title: 'Venues',
            url: 'venues.tpl.html'
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
        
        function recentMonth(obj) {
        	return	 Math.abs(($scope.now.getTime() - new Date(obj.startAt).getTime())/(24 * 60 * 60 * 1000)) <=31;
        }
        
        // 1. association basic information
        vm.association = Association.get({id:$state.params.id});
        // 2. all associate division
        vm.divisions = AssociationExt.queryDivisions({id:$state.params.id});
        // 3. all associate clubs
        vm.clubs = AssociationExt.queryClubs({id:$state.params.id});
        // 3. all associate players
        vm.players = AssociationExt.queryPlayers({id:$state.params.id});
    }
})();
