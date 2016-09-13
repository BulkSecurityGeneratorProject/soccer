(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubController', ClubController);

    ClubController.$inject = ['$scope', '$state', 'Club'];

    function ClubController ($scope, $state, Club) {
        var vm = this;
        
        vm.clubs = [];

        loadAll();

        function loadAll() {
            Club.query(function(result) {
                vm.clubs = result;
            });
        }
    }
})();
