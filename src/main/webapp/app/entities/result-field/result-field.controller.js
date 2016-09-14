(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('ResultFieldController', ResultFieldController);

    ResultFieldController.$inject = ['$scope', '$state', 'ResultField'];

    function ResultFieldController ($scope, $state, ResultField) {
        var vm = this;
        
        vm.resultFields = [];

        loadAll();

        function loadAll() {
            ResultField.query(function(result) {
                vm.resultFields = result;
            });
        }
    }
})();
