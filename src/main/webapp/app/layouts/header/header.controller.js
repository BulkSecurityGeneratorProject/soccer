(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['$state','Association'];

    function HeaderController ($state,Association) {
        var vm = this;
        vm.associations = [];
        
        loadAll();

        function loadAll() {
            Association.query(function(result) {
                vm.associations = result;
            });
        }
    }
})();
