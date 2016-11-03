(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationDashboardController', AssociationDashboardController);

    AssociationDashboardController.$inject = ['$scope','$state','Association'];

    function AssociationDashboardController ($scope, $state,Association) {
        var vm = this;
        // 引用其他的Service组合成Dashboard
        vm.association = Association.get({id : $state.params.id});
    }
})();
