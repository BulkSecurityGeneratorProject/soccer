(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicPlayersController', PublicPlayersController);

    PublicPlayersController.$inject = ['$scope','$state','ParseLinks','pagingParams','paginationConstants','Association','AssociationExt','AlertService'];

    function PublicPlayersController ($scope, $state,ParseLinks,pagingParams, paginationConstants,Association,AssociationExt,AlertService) {
        var vm = this;
        vm.associations = [];
        vm.association = {};
        vm.clubs = [];
        vm.club = {};

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        vm.players = null;

        if(pagingParams.id){
            vm.association.id = pagingParams.id;
        }

        vm.query = loadAll;
        vm.loadClubs = loadClubs;

        loadBasic();
        loadAll();
        function loadBasic () {
           Association.query(function(result) {
                vm.associations = result;
            });
           loadClubs();
        }

        function loadClubs () {
            var associationId = 1;
            if(vm.association && vm.association.id != null){
                associationId = vm.association.id;
            }
            AssociationExt.queryClubs({id:associationId},function(result){
                vm.clubs = result;
            });
        }

        function loadAll(){
            vm.isQuerying = true;

            if(vm.association.id!=null){
                AssociationExt.queryPlayers({
                    id:vm.association.id,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                },onSuccess, onError);
            }
            function sort() {
                var result = [vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc')];
                if (vm.predicate !== 'id') {
                    result.push('id');
                }
                return result;
            }
            function onSuccess(data, headers) {
                vm.links = ParseLinks.parse(headers('link'));
                vm.totalItems = headers('X-Total-Count');
                vm.queryCount = vm.totalItems;
                vm.players = data;
                vm.page = pagingParams.page;
                vm.isQuerying = false;
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }

        }

        function loadPage (page) {
            vm.page = page;
            vm.transition();
        }

        function transition () {
            $state.transitionTo($state.$current, {
                id: $state.params.id,
                page: vm.page,
                // sort: vm.predicate + ',' + (vm.reverse ? 'asc' : 'desc'),
                search: vm.currentSearch
            });
        }
    }
})();
