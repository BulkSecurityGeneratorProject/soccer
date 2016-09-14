(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultDataController', ResultDataController);

    ResultDataController.$inject = ['$scope', '$state', 'ResultData'];

    function ResultDataController ($scope, $state, ResultData) {
        var vm = this;
        
        vm.resultData = [];

        loadAll();

        function loadAll() {
            ResultData.query(function(result) {
                vm.resultData = result;
            });
        }
    }
})();
