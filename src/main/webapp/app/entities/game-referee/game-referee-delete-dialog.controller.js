(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('GameRefereeDeleteController',GameRefereeDeleteController);

    GameRefereeDeleteController.$inject = ['$uibModalInstance', 'entity', 'GameReferee'];

    function GameRefereeDeleteController($uibModalInstance, entity, GameReferee) {
        var vm = this;

        vm.gameReferee = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            GameReferee.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
