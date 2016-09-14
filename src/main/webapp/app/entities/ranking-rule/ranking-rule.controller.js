(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RankingRuleController', RankingRuleController);

    RankingRuleController.$inject = ['$scope', '$state', 'RankingRule'];

    function RankingRuleController ($scope, $state, RankingRule) {
        var vm = this;
        
        vm.rankingRules = [];

        loadAll();

        function loadAll() {
            RankingRule.query(function(result) {
                vm.rankingRules = result;
            });
        }
    }
})();
