(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultDataListEditController', ResultDataListEditController);

    ResultDataListEditController.$inject = ['$scope','$state','Game'];

    function ResultDataListEditController($scope,$state,Game) {
        var vm = this;

        // 1. Get game information
        vm.game = Game.get({id:$state.params.id});
        
        // 2. Get players information
        
        // 3. TODO
       

    }
})();
