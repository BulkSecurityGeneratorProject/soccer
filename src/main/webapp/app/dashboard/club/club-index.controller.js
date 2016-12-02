(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubDashboardController', ClubDashboardController);

    ClubDashboardController.$inject = ['$scope','$state','Club','ClubExt'];

    function ClubDashboardController ($scope, $state,Club,ClubExt) {
        var vm = this;
        // 引用其他的Service组合成Dashboard
        vm.club = Club.get({id : $state.params.id});
        // Schedule
        vm.games = ClubExt.queryGames({id : $state.params.id});
    }
})();
