(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionController', DivisionController);

    DivisionController.$inject = ['$scope', '$state', 'Division'];

    function DivisionController ($scope, $state, Division) {
        var vm = this;
        
        vm.divisions = [];

        loadAll();

        function loadAll() {
            Division.query(function(result) {
                vm.divisions = result;
            });
        }
    }
})();
