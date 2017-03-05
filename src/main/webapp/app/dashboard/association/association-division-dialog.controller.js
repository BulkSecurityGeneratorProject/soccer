(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('AssociationDivisionDialogController', AssociationDivisionDialogController);

    AssociationDivisionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Division', 'Dict', 'Association', 'RankingRule'];

    function AssociationDivisionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Division, Dict, Association, RankingRule) {
        var vm = this;

        vm.division = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.dicts = Dict.query();
        vm.division.association = Association.get($stateParams);
        vm.rankingrules = RankingRule.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.division.id !== null) {
                Division.update(vm.division, onSaveSuccess, onSaveError);
            } else {
                Division.save(vm.division, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:divisionUpdate', result);
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
