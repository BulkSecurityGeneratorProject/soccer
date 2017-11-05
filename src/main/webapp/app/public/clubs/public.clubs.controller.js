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

        function loadAll () {
           Association.query(function(result) {
                vm.associations = result;
            });
        }

        function loadClubs(){
            vm.isQuerying = true;

            if(vm.association.id!=null){
                AssociationExt.queryClubs({id:vm.association.id},function(result){
                    vm.clubs = result;
                    vm.isQuerying = false;
                });
            }
            
        }
    }
})();
