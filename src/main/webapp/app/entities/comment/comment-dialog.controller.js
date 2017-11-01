(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('CommentDialogController', CommentDialogController);

    CommentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Comment', 'Article'];

    function CommentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Comment, Article) {
        var vm = this;

        vm.comment = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.articles = Article.query();
        vm.comments = Comment.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.comment.id !== null) {
                Comment.update(vm.comment, onSaveSuccess, onSaveError);
            } else {
                Comment.save(vm.comment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:commentUpdate', result);
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
