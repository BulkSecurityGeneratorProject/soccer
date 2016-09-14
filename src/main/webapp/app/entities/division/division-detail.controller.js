(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionDetailController', DivisionDetailController);

    DivisionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Division', 'Dict', 'Association'];

    function DivisionDetailController($scope, $rootScope, $stateParams, previousState, entity, Division, Dict, Association) {
        var vm = this;

        vm.division = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:divisionUpdate', function(event, result) {
            vm.division = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
