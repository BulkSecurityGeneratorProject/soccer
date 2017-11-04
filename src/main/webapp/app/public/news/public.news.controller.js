(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicNewsController', PublicNewsController);

    PublicNewsController.$inject = ['$scope','$state',  'entity'];

    function PublicNewsController ($scope, $state, entity) {
        var vm = this;
        vm.article = entity;
        console.log(entity);
    }
})();
