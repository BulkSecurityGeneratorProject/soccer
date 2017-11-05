(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicPlayersController', PublicPlayersController);

    PublicPlayersController.$inject = ['$scope','$state','Association','AssociationExt'];

    function PublicPlayersController ($scope, $state,Association,AssociationExt) {
        var vm = this;
        vm.associations = [];
        vm.association = {};
        vm.clubs = [];
        vm.club = {};

        vm.players = null;
       
        vm.query = loadPlayers;

        loadAll();

        function loadAll () {
           Association.query(function(result) {
                vm.associations = result;
            });
           AssociationExt.queryClubs({id:1},function(result){
                vm.clubs = result;
           });
        }

        function loadPlayers(){
            vm.isQuerying = true;

            if(vm.association.id!=null){
                AssociationExt.queryPlayers({id:vm.association.id},function(result){
                    vm.players = result;
                    vm.isQuerying = false;
                });
            }
            
        }
    }
})();