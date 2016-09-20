(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadPlayerController', SquadPlayerController);

    SquadPlayerController.$inject = ['$scope', '$state', 'SquadPlayer'];

    function SquadPlayerController ($scope, $state, SquadPlayer) {
        var vm = this;
        
        vm.squadPlayers = [];

        loadAll();

        function loadAll() {
            SquadPlayer.query(function(result) {
                vm.squadPlayers = result;
            });
        }
    }
})();
