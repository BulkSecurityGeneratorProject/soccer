(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicResultDetailController', PublicResultDetailController);

    PublicResultDetailController.$inject = ['$scope','$state','ParseLinks','pagingParams', 'paginationConstants','Association','Division','Club','AssociationExt'];

    function PublicResultDetailController ($scope, $state,ParseLinks,pagingParams, paginationConstants,Association,Division,Club,AssociationExt) {
        var vm = this;

        loadAll();


        function loadAll () {

        }

    }
})();
