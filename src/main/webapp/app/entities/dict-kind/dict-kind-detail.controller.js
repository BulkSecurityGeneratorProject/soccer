(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictKindDetailController', DictKindDetailController);

    DictKindDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'DictKind'];

    function DictKindDetailController($scope, $rootScope, $stateParams, previousState, entity, DictKind) {
        var vm = this;

        vm.dictKind = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:dictKindUpdate', function(event, result) {
            vm.dictKind = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
