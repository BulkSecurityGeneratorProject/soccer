(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationController', AssociationController);

    AssociationController.$inject = ['$scope', '$state', 'Association'];

    function AssociationController ($scope, $state, Association) {
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
