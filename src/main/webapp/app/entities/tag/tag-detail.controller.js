(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('TagDetailController', TagDetailController);

    TagDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tag', 'Association', 'Club', 'Article'];

    function TagDetailController($scope, $rootScope, $stateParams, previousState, entity, Tag, Association, Club, Article) {
        var vm = this;

        vm.tag = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:tagUpdate', function(event, result) {
            vm.tag = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
