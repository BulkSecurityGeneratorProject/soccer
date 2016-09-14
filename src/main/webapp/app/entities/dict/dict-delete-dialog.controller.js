(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictDeleteController',DictDeleteController);

    DictDeleteController.$inject = ['$uibModalInstance', 'entity', 'Dict'];

    function DictDeleteController($uibModalInstance, entity, Dict) {
        var vm = this;

        vm.dict = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Dict.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
