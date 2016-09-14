(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictKindDeleteController',DictKindDeleteController);

    DictKindDeleteController.$inject = ['$uibModalInstance', 'entity', 'DictKind'];

    function DictKindDeleteController($uibModalInstance, entity, DictKind) {
        var vm = this;

        vm.dictKind = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            DictKind.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
