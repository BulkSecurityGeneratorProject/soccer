(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubTeamController', ClubTeamController);

    ClubTeamController.$inject = ['$scope','$state','ClubExt'];

    function ClubTeamController ($scope, $state,ClubExt) {
        var vm = this;
        
        vm.teams = [];
        loadAll();

        function loadAll() {
        	ClubExt.queryTeams({id : $state.params.id},function(result) {
                vm.teams = result;
            });
        }
    }
})();
