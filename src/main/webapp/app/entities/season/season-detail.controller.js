(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('SeasonDetailController', SeasonDetailController);

    SeasonDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Season'];

    function SeasonDetailController($scope, $rootScope, $stateParams, previousState, entity, Season) {
        var vm = this;

        vm.season = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:seasonUpdate', function(event, result) {
            vm.season = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
