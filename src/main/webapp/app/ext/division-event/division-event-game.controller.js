(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('DivisionEventGameController', DivisionEventGameController)
        .filter('htmlContent',['$sce', function($sce) {
			return function(input) {
				return $sce.trustAsHtml(input);
			}
		}]);

    DivisionEventGameController.$inject = ['$state', 'DivisionEventExt'];

    function DivisionEventGameController ($state,DivisionEventExt) {
        var vm = this;
        
        vm.divisionEventId = $state.params.id;
      
        vm.games = [];
        
        loadAll();
        
        function loadAll() {
            DivisionEventExt.queryGames($state.params,function(result) {
                vm.games = result;
            });
        }
    }
})();
