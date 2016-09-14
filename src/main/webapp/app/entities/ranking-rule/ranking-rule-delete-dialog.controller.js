(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RankingRuleDeleteController',RankingRuleDeleteController);

    RankingRuleDeleteController.$inject = ['$uibModalInstance', 'entity', 'RankingRule'];

    function RankingRuleDeleteController($uibModalInstance, entity, RankingRule) {
        var vm = this;

        vm.rankingRule = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            RankingRule.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
