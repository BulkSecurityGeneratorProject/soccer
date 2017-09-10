(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ClubSigninDialogController', ClubSigninDialogController);

    ClubSigninDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Club', 'Venue'];

    function ClubSigninDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Club, Venue) {
        var vm = this;
        
        vm.club = {};
        vm.association = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
       
        vm.venues = Venue.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            vm.club.association = vm.association;
            Club.save(vm.club, onSaveSuccess, onSaveError);
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
