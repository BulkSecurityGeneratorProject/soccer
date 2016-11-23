(function() {
    'use strict';

    angular
        .module('soccerApp')
        .controller('PublicDivisionController', PublicDivisionController);

    PublicDivisionController.$inject = ['$scope','$state'];

    function PublicDivisionController ($scope, $state) {
        var vm = this;
        $scope.now = new Date();
        
        
    }
})();
