(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('CatalogDialogController', CatalogDialogController);

    CatalogDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Catalog', 'Association', 'Club', 'Article'];

    function CatalogDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Catalog, Association, Club, Article) {
        var vm = this;

        vm.catalog = entity;
        vm.clear = clear;
        vm.save = save;
        vm.catalogs = Catalog.query();
        vm.associations = Association.query();
        vm.clubs = Club.query();
        vm.articles = Article.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.catalog.id !== null) {
                Catalog.update(vm.catalog, onSaveSuccess, onSaveError);
            } else {
                Catalog.save(vm.catalog, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:catalogUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
