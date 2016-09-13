(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PlayerController', PlayerController);

    PlayerController.$inject = ['$scope', '$state', 'Player'];

    function PlayerController ($scope, $state, Player) {
        var vm = this;
        
        vm.players = [];

        loadAll();

        function loadAll() {
            Player.query(function(result) {
                vm.players = result;
            });
        }
    }
})();
