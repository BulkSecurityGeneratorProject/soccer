(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SeasonController', SeasonController);

    SeasonController.$inject = ['$scope', '$state', 'Season'];

    function SeasonController ($scope, $state, Season) {
        var vm = this;
        
        vm.seasons = [];

        loadAll();

        function loadAll() {
            Season.query(function(result) {
                vm.seasons = result;
            });
        }
    }
})();
