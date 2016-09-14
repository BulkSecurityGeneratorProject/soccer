(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubDialogController', ClubDialogController);

    ClubDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Club', 'Association', 'Venue'];

    function ClubDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Club, Association, Venue) {
        var vm = this;

        vm.club = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.associations = Association.query();
        vm.venues = Venue.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.club.id !== null) {
                Club.update(vm.club, onSaveSuccess, onSaveError);
            } else {
                Club.save(vm.club, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:clubUpdate', result);
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
