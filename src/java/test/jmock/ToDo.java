package test.jmock;

public class ToDo {

    //TODO JavaDoc all classes in root org.jmock package.
    //TODO Find cool new name for "builder" API and rename packages
    //TODO Clean up org.jmock.expecation package.
    //TODO Make jmock.expectation package use Constraints instead of calling equals on expected value
    //TODO Better tests for error messages
    //DONE Implement matchers for call ordering.
	//TODO Add method to builder API for ordering calls across mocks
	//TODO Implement API for call ordering in dynamock API
    //TODO REMOVE ALL TO-DO COMMENTS
    //DONE Extract NoArgs and AnyArgs matcher classes from C
    //DONE Stop core packages relying on C
    //TODO Put low-level hook methods into builder interfaces (just need to be able to add Invokables to Mock)
    //TODO Remove getters from Invocation ? 
        
    // Sensible defaults to reduce test brittleness:
    //TODO Sensible default: If no expectation/match setup, invocation should produce no-op instead of fail.
    //TODO ExpectNotCalled (or equivalent) makes tests more expressive.
    
    // Suggested improvements:
    // Sensible defaults reduce test brittleness:
    // - Sensible default: When no args specified in Mock, use ANY-args instead of NO-args.
    //  -- I disagree: if you allow any arguments, putting C.ANY_ARGS shows what you are doing and isn't particularly verbose
	//  -- But, this is the behaviour of the builder API
	//  -- But but, it is a very subtle change from com.mockobjects API
    // - Extract interface from Invocation.
    //  -- WHY? It's a pure value object with no behaviour.
	//  -- But, could add useful behaviour to it
                  
}
