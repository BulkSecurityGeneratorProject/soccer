(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('CatalogDetailController', CatalogDetailController);

    CatalogDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Catalog', 'Association', 'Club', 'Article'];

    function CatalogDetailController($scope, $rootScope, $stateParams, previousState, entity, Catalog, Association, Club, Article) {
        var vm = this;

        vm.catalog = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:catalogUpdate', function(event, result) {
            vm.catalog = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
