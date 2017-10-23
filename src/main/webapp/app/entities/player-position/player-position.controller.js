(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PlayerPositionController', PlayerPositionController);

    PlayerPositionController.$inject = ['$scope', '$state', 'PlayerPosition'];

    function PlayerPositionController ($scope, $state, PlayerPosition) {
        var vm = this;
        
        vm.playerPositions = [];

        loadAll();

        function loadAll() {
            PlayerPosition.query(function(result) {
                vm.playerPositions = result;
            });
        }
    }
})();
