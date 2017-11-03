(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state','DivisionEventTable','Article','AlertService'];

    function HomeController ($scope, Principal, LoginService, $state,DivisionEventTable,Article,AlertService) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        // here always query divistion event 1 as first support division
        vm.divisionTable = DivisionEventTable.query({id:1});
        vm.articles = []

        $scope.$on('authenticationSuccess', function() {
            getAccount();
        });

        getAccount();
        loadAll();
        
        function getAccount() {
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }
        function register () {
            $state.go('register');
        }

        function loadAll () {
            Article.query({
                page: 0,
                size: 10,
                sort: sort()
            }, onSuccess, onError);
            function sort() {
                var result = ['id,asc'];
                return result;
            }
            function onSuccess(data, headers) {
                vm.articles = data;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }
        }
    }
})();
