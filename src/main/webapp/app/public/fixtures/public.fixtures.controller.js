(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicFixturesController', PublicFixturesController);

    PublicFixturesController.$inject = ['$scope','$state','ParseLinks','pagingParams', 'paginationConstants','Association','Division','Club','AssociationExt'];

    function PublicFixturesController ($scope, $state,ParseLinks,pagingParams, paginationConstants,Association,Division,Club,AssociationExt) {
        var vm = this;
        vm.associations = [];
        vm.divisions = [];
        vm.clubs = [];
        vm.games = [];

        vm.association = {};
        vm.division = {};
        vm.club = {};

        vm.loadPage = loadPage;
        vm.predicate = pagingParams.predicate;
        vm.reverse = pagingParams.ascending;
        vm.transition = transition;
        vm.itemsPerPage = paginationConstants.itemsPerPage;

        vm.query = loadFixtures;
        vm.loadQueryData = loadQueryData;

        if(pagingParams.id){
            vm.association.id = pagingParams.id;
        }

        loadAll();

        loadFixtures();


        function loadAll () {
           Association.query(function(result) {
                vm.associations = result;
            });
            loadQueryData();
        }

        function loadQueryData(){
            var associationId = 1;
            if(vm.association.id != null){
                associationId = vm.association.id;
            }
            AssociationExt.queryDivisionEvents({id:associationId},function(result){
                vm.divisions = result;
            });


            AssociationExt.queryClubs({id:associationId},function(result){
                vm.clubs = result;
            });
        }

        function loadFixtures(){
            vm.isQuerying = true;

            AssociationExt.queryAssociationFixtures({
                    id:vm.association.id,
                    page: pagingParams.page - 1,
                    size: vm.itemsPerPage,
                    sort: sort()
                },onSuccess,onError);

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
                vm.games = data;
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
                search: vm.currentSearch
            });
        }
    }
})();
