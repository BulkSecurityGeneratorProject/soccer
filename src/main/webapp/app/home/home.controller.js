(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','DivisionEventTable'];

    function HomeController ($scope, Principal, LoginService, $state,DivisionEventTable) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        // here always query divistion event 1 as first support division
        vm.divisionTable = DivisionEventTable.query({id:1});

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();

        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }
    }
})();
