(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationDivisionController', AssociationDivisionController);

    AssociationDivisionController.$inject = ['$scope','$state','Association','AssociationExt'];

    function AssociationDivisionController ($scope, $state,Association,AssociationExt) {
        var vm = this;
        vm.divisions = [];
        AssociationExt.queryDivisions($state.params,function(result) {
        	vm.divisions = result;
        	vm.id = $state.params.id;
        });
    }
})();
