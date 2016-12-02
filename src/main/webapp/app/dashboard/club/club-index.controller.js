(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubDashboardController', ClubDashboardController);

    ClubDashboardController.$inject = ['$scope','$state','Club'];

    function ClubDashboardController ($scope, $state,Club) {
        var vm = this;
        // 引用其他的Service组合成Dashboard
        vm.club = Club.get({id : $state.params.id});
    }
})();
