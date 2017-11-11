(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicFixturesController', PublicFixturesController);

    PublicFixturesController.$inject = ['$scope','$state','Association','Division','Club','AssociationExt'];

    function PublicFixturesController ($scope, $state,Association,Division,Club,AssociationExt) {
        var vm = this;
        vm.associations = [];
        vm.divisions = [];
        vm.clubs = [];
        vm.games = [];

        vm.association = {};
        vm.division = {};
        vm.club = {};

        vm.query = loadFixtures;
        vm.loadQueryData = loadQueryData;

        loadAll();

        loadFixtures();


        function loadAll () {
           Association.query(function(result) {
                vm.associations = result;
            });
            loadQueryData();
        }

        function loadQueryData(){
            var associationId = 1;
            if(vm.association.id != null){
                associationId = vm.association.id;
            }
            AssociationExt.queryDivisionEvents({id:associationId},function(result){
                vm.divisions = result;
            });


            AssociationExt.queryClubs({id:associationId},function(result){
                vm.clubs = result;
            });
        }

        function loadFixtures(){
            vm.isQuerying = true;
            var associationId = 1;
            if(vm.association.id != null){
                associationId = vm.association.id;
            }

            AssociationExt.queryAssociationFixtures({id:associationId},function(result){
                vm.games = result;
                vm.isQuerying = false;
            });

        }
    }
})();
