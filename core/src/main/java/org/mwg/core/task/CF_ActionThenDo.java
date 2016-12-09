package org.mwg.core.task;

import org.mwg.task.Action;
import org.mwg.task.ActionFunction;
import org.mwg.task.TaskContext;

class CF_ActionThenDo implements Action {

    private final ActionFunction _wrapped;

    CF_ActionThenDo(final ActionFunction p_wrapped) {
        if (p_wrapped == null) {
            throw new RuntimeException("action should not be null");
        }
        this._wrapped = p_wrapped;
    }

    @Override
    public void eval(final TaskContext ctx) {
        //execute wrapped task but does not call the next method of the wrapped context
        //this allow to have exactly one call to the Context.next method
        _wrapped.eval(ctx);
    }

    @Override
    public String toString() {
        return "then()";
    }

}