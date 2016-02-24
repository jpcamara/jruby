/*
 * Copyright (c) 2014, 2016 Oracle and/or its affiliates. All rights reserved. This
 * code is released under a tri EPL/GPL/LGPL license. You can use it,
 * redistribute it and/or modify it under the terms of the:
 *
 * Eclipse Public License version 1.0
 * GNU General Public License version 2
 * GNU Lesser General Public License version 2.1
 */
package org.jruby.truffle.language.globals;

import com.oracle.truffle.api.CompilerDirectives;
import com.oracle.truffle.api.frame.VirtualFrame;
import com.oracle.truffle.api.source.SourceSection;
import org.jruby.truffle.RubyContext;
import org.jruby.truffle.core.Layouts;
import org.jruby.truffle.core.module.ModuleOperations;
import org.jruby.truffle.language.RubyNode;
import org.jruby.truffle.language.control.RaiseException;

public class CheckStdoutVariableTypeNode extends RubyNode {

    @Child private RubyNode child;

    public CheckStdoutVariableTypeNode(RubyContext context, SourceSection sourceSection, RubyNode child) {
        super(context, sourceSection);
        this.child = child;
    }

    public Object execute(VirtualFrame frame) {
        CompilerDirectives.transferToInterpreter();

        final Object childValue = child.execute(frame);

        if (childValue == nil() || ModuleOperations.lookupMethod(coreLibrary().getMetaClass(childValue), "write") == null) {
            throw new RaiseException(coreLibrary().typeError(String.format("$stdout must have write method, %s given", Layouts.MODULE.getFields(coreLibrary().getLogicalClass(childValue)).getName()), this));
        }

        return childValue;
    }

}
