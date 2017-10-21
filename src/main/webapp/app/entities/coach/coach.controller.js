(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('CoachController', CoachController);

    CoachController.$inject = ['$scope', '$state', 'Coach'];

    function CoachController ($scope, $state, Coach) {
        var vm = this;
        
        vm.coaches = [];

        loadAll();

        function loadAll() {
            Coach.query(function(result) {
                vm.coaches = result;
            });
        }
    }
})();
