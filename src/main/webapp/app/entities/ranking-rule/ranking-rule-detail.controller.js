(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('RankingRuleDetailController', RankingRuleDetailController);

    RankingRuleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'RankingRule', 'Dict'];

    function RankingRuleDetailController($scope, $rootScope, $stateParams, previousState, entity, RankingRule, Dict) {
        var vm = this;

        vm.rankingRule = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('soccerApp:rankingRuleUpdate', function(event, result) {
            vm.rankingRule = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
