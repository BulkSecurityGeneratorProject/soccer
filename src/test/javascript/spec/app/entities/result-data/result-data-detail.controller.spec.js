'use strict';

describe('Controller Tests', function() {

    describe('ResultData Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockResultData, MockGame, MockSquadPlayer, MockResultField;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockResultData = jasmine.createSpy('MockResultData');
            MockGame = jasmine.createSpy('MockGame');
            MockSquadPlayer = jasmine.createSpy('MockSquadPlayer');
            MockResultField = jasmine.createSpy('MockResultField');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ResultData': MockResultData,
                'Game': MockGame,
                'SquadPlayer': MockSquadPlayer,
                'ResultField': MockResultField
            };
            createController = function() {
                $injector.get('$controller')("ResultDataDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'soccerApp:resultDataUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
