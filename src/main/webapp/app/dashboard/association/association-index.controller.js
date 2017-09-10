(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationDashboardController', AssociationDashboardController);

    AssociationDashboardController.$inject = ['$scope','$state','Association','AssociationExt'];

    function AssociationDashboardController ($scope, $state,Association,AssociationExt) {
        var vm = this;
        // 引用其他的Service组合成Dashboard
        vm.association = Association.get({id : $state.params.id});
        AssociationExt.queryDivisions($state.params,function(result) {
            vm.divisionCount = result.length;
        });
        AssociationExt.queryClubs($state.params,function(result) {
            vm.clubCount = result.length;
        });
        AssociationExt.queryPlayers($state.params,function(result) {
            vm.playerCount = result.length;
        });
    }
})();
