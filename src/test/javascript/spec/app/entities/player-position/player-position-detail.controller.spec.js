'use strict';

describe('Controller Tests', function() {

    describe('PlayerPosition Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPlayerPosition, MockPlayer, MockDict;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPlayerPosition = jasmine.createSpy('MockPlayerPosition');
            MockPlayer = jasmine.createSpy('MockPlayer');
            MockDict = jasmine.createSpy('MockDict');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'PlayerPosition': MockPlayerPosition,
                'Player': MockPlayer,
                'Dict': MockDict
            };
            createController = function() {
                $injector.get('$controller')("PlayerPositionDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:playerPositionUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
