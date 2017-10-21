(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameRefereeController', GameRefereeController);

    GameRefereeController.$inject = ['$scope', '$state', 'GameReferee'];

    function GameRefereeController ($scope, $state, GameReferee) {
        var vm = this;
        
        vm.gameReferees = [];

        loadAll();

        function loadAll() {
            GameReferee.query(function(result) {
                vm.gameReferees = result;
            });
        }
    }
})();
