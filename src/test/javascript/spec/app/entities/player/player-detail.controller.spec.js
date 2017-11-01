'use strict';

describe('Controller Tests', function() {

    describe('Player Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockPlayer, MockTeam, MockDict, MockAssociation;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockPlayer = jasmine.createSpy('MockPlayer');
            MockTeam = jasmine.createSpy('MockTeam');
            MockDict = jasmine.createSpy('MockDict');
            MockAssociation = jasmine.createSpy('MockAssociation');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Player': MockPlayer,
                'Team': MockTeam,
                'Dict': MockDict,
                'Association': MockAssociation
            };
            createController = function() {
                $injector.get('$controller')("PlayerDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:playerUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
