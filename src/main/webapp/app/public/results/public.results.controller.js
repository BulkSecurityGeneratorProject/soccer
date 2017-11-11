(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicResultsController', PublicResultsController);

    PublicResultsController.$inject = ['$scope','$state','Association','Division','Club','AssociationExt'];

    function PublicResultsController ($scope, $state,Association,Division,Club,AssociationExt) {
        var vm = this;
        vm.associations = [];
        vm.divisions = [];
        vm.clubs = [];
        vm.games = [];

        vm.association = {};
        vm.division = {};
        vm.club = {};

        vm.query = loadResults;
        vm.loadQueryData = loadQueryData;

        loadAll();

        loadResults();


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

        function loadResults(){
            vm.isQuerying = true;
            var associationId = 1;
            if(vm.association.id != null){
                associationId = vm.association.id;
            }

            AssociationExt.queryAssociationResults({id:associationId},function(result){
                vm.games = result;
                vm.isQuerying = false;
            });

        }
    }
})();
