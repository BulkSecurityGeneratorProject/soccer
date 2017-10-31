(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ArticleCommentDetailController', ArticleCommentDetailController);

    ArticleCommentDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ArticleComment', 'Article'];

    function ArticleCommentDetailController($scope, $rootScope, $stateParams, previousState, entity, ArticleComment, Article) {
        var vm = this;

        vm.articleComment = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:articleCommentUpdate', function(event, result) {
            vm.articleComment = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
