'use strict';

describe('Controller Tests', function() {

    describe('SquadPlayer Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSquadPlayer, MockSquad, MockPlayer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSquadPlayer = jasmine.createSpy('MockSquadPlayer');
            MockSquad = jasmine.createSpy('MockSquad');
            MockPlayer = jasmine.createSpy('MockPlayer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SquadPlayer': MockSquadPlayer,
                'Squad': MockSquad,
                'Player': MockPlayer
            };
            createController = function() {
                $injector.get('$controller')("SquadPlayerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:squadPlayerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
