(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('HeaderController', HeaderController);

    HeaderController.$inject = ['$state','Association','Auth', 'Principal', 'ProfileService', 'LoginService'];

    function HeaderController ($state,Association,Auth, Principal, ProfileService, LoginService) {
        var vm = this;
        vm.associations = [];
        vm.isNavbarCollapsed = true;
        vm.isAuthenticated = Principal.isAuthenticated;

        ProfileService.getProfileInfo().then(function(response) {
            vm.inProduction = response.inProduction;
            vm.swaggerEnabled = response.swaggerEnabled;
        });
        
        loadAll();

        function login() {
            collapseNavbar();
            LoginService.open();
        }

        function logout() {
            collapseNavbar();
            Auth.logout();
            $state.go('home');
        }

        function toggleNavbar() {
            vm.isNavbarCollapsed = !vm.isNavbarCollapsed;
        }

        function collapseNavbar() {
            vm.isNavbarCollapsed = true;
        }
        

        function loadAll() {
            Association.query(function(result) {
                vm.associations = result;
            });
        }
    }
})();
