(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ArticleCommentController', ArticleCommentController);

    ArticleCommentController.$inject = ['$scope', '$state', 'ArticleComment'];

    function ArticleCommentController ($scope, $state, ArticleComment) {
        var vm = this;
        
        vm.articleComments = [];

        loadAll();

        function loadAll() {
            ArticleComment.query(function(result) {
                vm.articleComments = result;
            });
        }
    }
})();
