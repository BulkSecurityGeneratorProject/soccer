(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubDivisionEventController', ClubDivisionEventController);

    ClubDivisionEventController.$inject = ['$scope','$state','ClubExt'];

    function ClubDivisionEventController ($scope, $state,ClubExt) {
        var vm = this;
        vm.divisionEvents = [];
        ClubExt.queryDivisionEvents($state.params,function(result) {
        	vm.divisionEvents = result;
        	vm.id = $state.params.id;
        });
    }
})();
