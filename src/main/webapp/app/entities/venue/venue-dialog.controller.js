(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('VenueDialogController', VenueDialogController);

    VenueDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Venue'];

    function VenueDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Venue) {
        var vm = this;

        vm.venue = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.venue.id !== null) {
                Venue.update(vm.venue, onSaveSuccess, onSaveError);
            } else {
                Venue.save(vm.venue, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('soccerApp:venueUpdate', result);
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
