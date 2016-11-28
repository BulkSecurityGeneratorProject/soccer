(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('LineupController', LineupController);

    LineupController.$inject = ['$scope', '$state', 'Lineup'];

    function LineupController ($scope, $state, Lineup) {
        var vm = this;
        
        vm.lineups = [];

        loadAll();

        function loadAll() {
            Lineup.query(function(result) {
                vm.lineups = result;
            });
        }
    }
})();
