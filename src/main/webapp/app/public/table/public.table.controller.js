(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicTableController', PublicTableController);

    PublicTableController.$inject = ['$scope','$state','Division','Season','DivisionEventTable'];

    function PublicTableController ($scope, $state,Division,Season,DivisionEventTable) {
        var vm = this;
        vm.divisions = [];
        vm.seasons = [];

        vm.division = null;
        vm.season = null;
        vm.divisionTable = [];
        vm.query = loadDivisionEventTable;

        loadAll();

        if(vm.division && vm.season){
            loadDivisionEventTable();
        }

        function loadAll () {
           Division.query(function(result) {
                vm.divisions = result;
            });
           Season.query(function(result){
                vm.seasons = result;
           });

        }

        function loadDivisionEventTable(){
            vm.isQuerying = true;

            if(vm.division.id!=null && vm.season.id!=null){
                DivisionEventTable.query({id:1},function(result){
                    vm.divisionTable = result;
                    vm.isQuerying = false;
                });
            }
            
        }
    }
})();
