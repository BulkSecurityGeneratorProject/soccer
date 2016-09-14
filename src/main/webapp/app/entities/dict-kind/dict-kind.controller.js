(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DictKindController', DictKindController);

    DictKindController.$inject = ['$scope', '$state', 'DictKind'];

    function DictKindController ($scope, $state, DictKind) {
        var vm = this;
        
        vm.dictKinds = [];

        loadAll();

        function loadAll() {
            DictKind.query(function(result) {
                vm.dictKinds = result;
            });
        }
    }
})();
