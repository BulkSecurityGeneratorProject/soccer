(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('VenueDeleteController',VenueDeleteController);

    VenueDeleteController.$inject = ['$uibModalInstance', 'entity', 'Venue'];

    function VenueDeleteController($uibModalInstance, entity, Venue) {
        var vm = this;

        vm.venue = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Venue.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
