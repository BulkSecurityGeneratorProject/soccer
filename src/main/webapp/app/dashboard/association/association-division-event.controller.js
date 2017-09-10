(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationDivisionEventController', AssociationDivisionEventController);

    AssociationDivisionEventController.$inject = ['$scope','$state','Association','AssociationExt'];

    function AssociationDivisionEventController ($scope, $state,Association,AssociationExt) {
        var vm = this;
        vm.divisionEvents = [];
        AssociationExt.queryDivisionEvents($state.params,function(result) {
        	vm.divisionEvents = result;
        	vm.id = $state.params.id;
        });
    }
})();
