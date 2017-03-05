(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubPlayerController', ClubPlayerController);

    ClubPlayerController.$inject = ['$scope','$state','ClubExt'];

    function ClubPlayerController ($scope, $state,ClubExt) {
        var vm = this;
        
        vm.players = [];
        loadAll();

        function loadAll() {
        	ClubExt.queryPlayers({id : $state.params.id},function(result) {
                vm.players = result;
            });
        }
    }
})();
