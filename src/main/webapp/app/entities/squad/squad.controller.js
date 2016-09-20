(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SquadController', SquadController);

    SquadController.$inject = ['$scope', '$state', 'Squad'];

    function SquadController ($scope, $state, Squad) {
        var vm = this;
        
        vm.squads = [];

        loadAll();

        function loadAll() {
            Squad.query(function(result) {
                vm.squads = result;
            });
        }
    }
})();
