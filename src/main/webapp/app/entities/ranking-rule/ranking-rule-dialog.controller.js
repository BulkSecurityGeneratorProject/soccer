(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RankingRuleDialogController', RankingRuleDialogController);

    RankingRuleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'RankingRule', 'Dict'];

    function RankingRuleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, RankingRule, Dict) {
        var vm = this;

        vm.rankingRule = entity;
        vm.clear = clear;
        vm.save = save;
        vm.dicts = Dict.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.rankingRule.id !== null) {
                RankingRule.update(vm.rankingRule, onSaveSuccess, onSaveError);
            } else {
                RankingRule.save(vm.rankingRule, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:rankingRuleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
