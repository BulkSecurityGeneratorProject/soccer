(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultDataDetailController', ResultDataDetailController);

    ResultDataDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ResultData', 'Game', 'Player', 'ResultField'];

    function ResultDataDetailController($scope, $rootScope, $stateParams, previousState, entity, ResultData, Game, Player, ResultField) {
        var vm = this;

        vm.resultData = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:resultDataUpdate', function(event, result) {
            vm.resultData = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
