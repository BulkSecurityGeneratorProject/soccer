(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ArticleDetailController', ArticleDetailController);

    ArticleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Article', 'Catalog', 'Association', 'Club', 'Tag'];

    function ArticleDetailController($scope, $rootScope, $stateParams, previousState, entity, Article, Catalog, Association, Club, Tag) {
        var vm = this;

        vm.article = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:articleUpdate', function(event, result) {
            vm.article = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
