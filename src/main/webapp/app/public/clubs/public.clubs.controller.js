(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicClubsController', PublicClubsController);

    PublicClubsController.$inject = ['$scope','$state','Association','AssociationExt'];

    function PublicClubsController ($scope, $state,Association,AssociationExt) {
        var vm = this;
        vm.associations = [];
        vm.association = {};
        vm.clubs = null;
       
        vm.query = loadClubs;

        loadAll();
        loadClubs();

        function loadAll () {
           Association.query(function(result) {
                vm.associations = result;
            });
        }

        function loadClubs(){
            vm.isQuerying = true;
            // default association
            var associationId = 1;
            if(vm.association.id != null){
                associationId = vm.association.id;
            }
            AssociationExt.queryClubs({id:associationId},function(result){
                vm.clubs = result;
                vm.isQuerying = false;
            });
            
        }
    }
})();
