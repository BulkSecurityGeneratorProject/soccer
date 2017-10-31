(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ArticleCommentDialogController', ArticleCommentDialogController);

    ArticleCommentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ArticleComment', 'Article'];

    function ArticleCommentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ArticleComment, Article) {
        var vm = this;

        vm.articleComment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.articles = Article.query();
        vm.articlecomments = ArticleComment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.articleComment.id !== null) {
                ArticleComment.update(vm.articleComment, onSaveSuccess, onSaveError);
            } else {
                ArticleComment.save(vm.articleComment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:articleCommentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createAt = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
