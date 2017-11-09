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

        vm.division = {};
        vm.season = {};
        vm.divisionTable = [];
        vm.query = loadDivisionEventTable;

        loadAll();

        
        loadDivisionEventTable();
        

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
            var divisionId = 1;// default division table
            if(vm.division.id!=null && vm.season.id!=null){
                divisionId = vm.division.id;
            }

            DivisionEventTable.query({id:divisionId},function(result){
                vm.divisionTable = result;
                vm.isQuerying = false;
            });
            
        }
    }
})();
