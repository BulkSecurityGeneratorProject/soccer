(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RefereeController', RefereeController);

    RefereeController.$inject = ['$scope', '$state', 'Referee'];

    function RefereeController ($scope, $state, Referee) {
        var vm = this;
        
        vm.referees = [];

        loadAll();

        function loadAll() {
            Referee.query(function(result) {
                vm.referees = result;
            });
        }
    }
})();
