package test.jmock;

public class ToDo {

    //TODO JavaDoc all classes in root org.jmock package.
    //TODO Find cool new name for "builder" API
    //TODO Clean up org.jmock.expecation package.
    //TODO Make jmock.expectation package use Constraints instead of calling equals on expected value
    //TODO DO SOMETHING WITH THE EMPTY TEST CLASSES IN test.jmock.dynamic
    //TODO Implement matchers for call ordering.
    //TODO REMOVE ALL TO-DO COMMENTS
    //Done Extract NoArgs and AnyArgs matcher classes from C
    //TODO Stop core packages relying on C
    //TODO Put low-level hook methods into builder interfaces
    //TODO Remove getters from Invocation ? 
        
    //    (sensible defaults reduce test brittleness)
    //TODO: Sensible default: If no expectation/match setup, invocation should produce no-op instead of fail.
    // TODO: ExpectNotCalled (or equivalent) makes tests more expressive.
    
    //        *** Suggested improvements ***
    // (sensible defaults reduce test brittleness)
    // - Sensible default: When no args specified in Mock, use ANY-args instead of NO-args.
    //  -- I disagree: if you allow any arguments, putting C.ANY_ARGS shows what you are doing and isn't particularly verbose
    // - Extract interface from Invocation.
    //  -- WHY? It's a pure value object with no behaviour.
                  
}
