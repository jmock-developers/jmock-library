package test.jmock;

public class ToDo {
    //TODO JavaDoc interfaces and those classes that need it.
    //TODO Find cool new name for "builder" API and rename packages ("hotmock" top candidate)
    //TODO Better tests for error messages
    //TODO Better format for expected calls in error messages
    //      - make expected and actual call signatures look similar
	//TODO Implement API for call ordering in dynamock API
    //TODO REMOVE ALL TO-DO COMMENTS
    //TODO Remove getters from Invocation (?)
	//TODO Extract interface from Invocation class (?)
    //TODO List expected/stubbed methods in verify errors
    
	//-----------------------------------------------------------------------------------------
	// Tasks completed
	
	//DONE Implement matchers for call ordering.
	//DONE Add method to builder API for ordering calls across mocks
	//DONE Extract NoArgs and AnyArgs matcher classes from C
	//DONE Stop core packages relying on C
	//DONE Put low-level hook methods into builder interfaces
	//DONE Sensible default: If no expectation/match setup, invocation should produce no-op instead of fail.
	//  - this is to be dropped following user feedback
	//DONE When no args specified in dynamock.Mock, use ANY-args instead of NO-args.
	//  - dropped methods without arg specification in dynamock because of this ambiguity
    //DONE Expose setDefaultStub through high-level API
    //DONE Default result stub should return zero-length arrays for array types
    //DONE Make name of mock more visible in error messages
    //DONE ExpectNotCalled (or equivalent) makes tests more expressive.
	
	//-----------------------------------------------------------------------------------------
	// Post 1.0 tasks
	
    //TODO Clean up org.jmock.expecation package.
	//TODO Make jmock.expectation package use Constraints instead of calling equals on expected value
}
