(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictController', DictController);

    DictController.$inject = ['$scope', '$state', 'Dict'];

    function DictController ($scope, $state, Dict) {
        var vm = this;
        
        vm.dicts = [];

        loadAll();

        function loadAll() {
            Dict.query(function(result) {
                vm.dicts = result;
            });
        }
    }
})();
