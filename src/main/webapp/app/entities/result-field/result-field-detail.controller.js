(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultFieldDetailController', ResultFieldDetailController);

    ResultFieldDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ResultField'];

    function ResultFieldDetailController($scope, $rootScope, $stateParams, previousState, entity, ResultField) {
        var vm = this;

        vm.resultField = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:resultFieldUpdate', function(event, result) {
            vm.resultField = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
