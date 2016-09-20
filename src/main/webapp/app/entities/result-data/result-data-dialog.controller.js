(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultDataDialogController', ResultDataDialogController);

    ResultDataDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ResultData', 'Game', 'SquadPlayer', 'ResultField'];

    function ResultDataDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ResultData, Game, SquadPlayer, ResultField) {
        var vm = this;

        vm.resultData = entity;
        vm.clear = clear;
        vm.save = save;
        vm.games = Game.query();
        vm.squadplayers = SquadPlayer.query();
        vm.resultfields = ResultField.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resultData.id !== null) {
                ResultData.update(vm.resultData, onSaveSuccess, onSaveError);
            } else {
                ResultData.save(vm.resultData, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:resultDataUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
