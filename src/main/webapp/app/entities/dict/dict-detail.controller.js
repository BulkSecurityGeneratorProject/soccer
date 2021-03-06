(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictDetailController', DictDetailController);

    DictDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Dict', 'DictKind', 'Player'];

    function DictDetailController($scope, $rootScope, $stateParams, previousState, entity, Dict, DictKind, Player) {
        var vm = this;

        vm.dict = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:dictUpdate', function(event, result) {
            vm.dict = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
