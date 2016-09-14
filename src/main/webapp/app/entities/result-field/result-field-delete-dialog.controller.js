(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultFieldDeleteController',ResultFieldDeleteController);

    ResultFieldDeleteController.$inject = ['$uibModalInstance', 'entity', 'ResultField'];

    function ResultFieldDeleteController($uibModalInstance, entity, ResultField) {
        var vm = this;

        vm.resultField = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ResultField.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
