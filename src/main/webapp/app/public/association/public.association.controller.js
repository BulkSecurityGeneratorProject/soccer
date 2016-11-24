(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicAssociationController', PublicAssociationController);

    PublicAssociationController.$inject = ['$scope','$state','Association'];

    function PublicAssociationController ($scope, $state,Association) {
        var vm = this;
        $scope.now = new Date();
        $scope.tabs = [{
            title: 'Overview',
            url: 'overview.tpl.html'
        },{
            title: 'Schduled',
            url: 'schduled.tpl.html'
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
    }
})();
