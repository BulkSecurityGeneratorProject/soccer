(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationDetailController', AssociationDetailController);

    AssociationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Association', 'Dict'];

    function AssociationDetailController($scope, $rootScope, $stateParams, previousState, entity, Association, Dict) {
        var vm = this;

        vm.association = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:associationUpdate', function(event, result) {
            vm.association = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
