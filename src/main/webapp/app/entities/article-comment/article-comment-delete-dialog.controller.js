(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ArticleCommentDeleteController',ArticleCommentDeleteController);

    ArticleCommentDeleteController.$inject = ['$uibModalInstance', 'entity', 'ArticleComment'];

    function ArticleCommentDeleteController($uibModalInstance, entity, ArticleComment) {
        var vm = this;

        vm.articleComment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ArticleComment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
