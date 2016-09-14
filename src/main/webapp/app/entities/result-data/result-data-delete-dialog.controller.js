(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultDataDeleteController',ResultDataDeleteController);

    ResultDataDeleteController.$inject = ['$uibModalInstance', 'entity', 'ResultData'];

    function ResultDataDeleteController($uibModalInstance, entity, ResultData) {
        var vm = this;

        vm.resultData = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ResultData.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
