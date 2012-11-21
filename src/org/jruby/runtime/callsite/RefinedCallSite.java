package org.jruby.runtime.callsite;

import org.jruby.IncludedModuleWrapper;
import org.jruby.Ruby;
import org.jruby.RubyModule;
import org.jruby.exceptions.JumpException;
import org.jruby.internal.runtime.methods.DynamicMethod;
import org.jruby.internal.runtime.methods.UndefinedMethod;
import org.jruby.parser.StaticScope;
import org.jruby.runtime.Block;
import org.jruby.runtime.CallType;
import org.jruby.runtime.ThreadContext;
import org.jruby.runtime.builtin.IRubyObject;

import java.util.Set;

public class RefinedCallSite extends CachingCallSite {
    final StaticScope refinementScope;

    public RefinedCallSite(String methodName, StaticScope refinementScope) {
        super(methodName, CallType.NORMAL);

        this.refinementScope = refinementScope;
    }

    private final CacheEntry findRefinedMethod(Ruby runtime, IRubyObject receiver, String name) {
        RubyModule recvClass = receiver.getMetaClass();
        
        if (cache != null && cache.token == runtime.getRefinementToken()) {
            return cache;
        }

        for (StaticScope currentScope = refinementScope; currentScope != null; currentScope = currentScope.getEnclosingScope()) {
            //System.out.println("searching scope: " + currentScope);

            if (currentScope.hasRefinements()) {
                // refined lookup
                Set<RubyModule> ndRefinements = currentScope.getRefinements();

                //System.out.println("refinements in scope: " + ndRefinements);

                for (; recvClass != null; recvClass = recvClass.getSuperClass()) {
                    RubyModule ancestor = recvClass;
                    if (recvClass instanceof IncludedModuleWrapper) ancestor = recvClass.getNonIncludedClass();

                    for (RubyModule refiner : ndRefinements) {
                        RubyModule refinement = refiner.refinementForModule(ancestor);

                        if (refinement != null) {
                            DynamicMethod method = refinement.searchMethod(name);

                            if (method != null && !method.isUndefined()) {
                                return cache = new CacheEntry(method, runtime.getRefinementToken());
                            }
                        }
                    }
                }
            }
        }

        return CacheEntry.NULL_CACHE;
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName);
        }

        return super.call(context, caller, self);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0);
        }

        return super.call(context, caller, self, arg0);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, IRubyObject arg1) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, arg1);
        }

        return super.call(context, caller, self, arg0, arg1);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, IRubyObject arg1, IRubyObject arg2) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, arg1, arg2);
        }

        return super.call(context, caller, self, arg0, arg1, arg2);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject... args) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, args);
        }

        return super.call(context, caller, self, args);
    }

    @Override
    public IRubyObject callVarargs(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject... args) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, args);
        }

        return super.callVarargs(context, caller, self, args);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, block);
        }

        return super.call(context, caller, self, block);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, block);
        }

        return super.call(context, caller, self, arg0, block);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, IRubyObject arg1, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, arg1, block);
        }

        return super.call(context, caller, self, arg0, arg1, block);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, IRubyObject arg1, IRubyObject arg2, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, arg1, arg2, block);
        }

        return super.call(context, caller, self, arg0, arg1, arg2, block);
    }

    @Override
    public IRubyObject call(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject[] args, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, args, block);
        }

        return super.call(context, caller, self, args, block);
    }

    @Override
    public IRubyObject callVarargs(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject[] args, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, args, block);
        }

        return super.call(context, caller, self, block);
    }

    @Override
    public IRubyObject callIter(ThreadContext context, IRubyObject caller, IRubyObject self, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            try {
                return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, block);
            } catch (JumpException.BreakJump bj) {
                return handleBreakJump(context, bj);
            } catch (JumpException.RetryJump rj) {
                throw retryJumpError(context);
            } finally {
                block.escape();
            }
        }

        return super.callIter(context, caller, self, block);
    }

    @Override
    public IRubyObject callIter(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            try {
                return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, block);
            } catch (JumpException.BreakJump bj) {
                return handleBreakJump(context, bj);
            } catch (JumpException.RetryJump rj) {
                throw retryJumpError(context);
            } finally {
                block.escape();
            }
        }

        return super.callIter(context, caller, self, arg0, block);
    }

    @Override
    public IRubyObject callIter(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, IRubyObject arg1, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            try {
                return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, arg1, block);
            } catch (JumpException.BreakJump bj) {
                return handleBreakJump(context, bj);
            } catch (JumpException.RetryJump rj) {
                throw retryJumpError(context);
            } finally {
                block.escape();
            }
        }

        return super.callIter(context, caller, self, arg0, arg1, block);
    }

    @Override
    public IRubyObject callIter(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject arg0, IRubyObject arg1, IRubyObject arg2, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            try {
                return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, arg0, arg1, arg2, block);
            } catch (JumpException.BreakJump bj) {
                return handleBreakJump(context, bj);
            } catch (JumpException.RetryJump rj) {
                throw retryJumpError(context);
            } finally {
                block.escape();
            }
        }

        return super.callIter(context, caller, self, arg0, arg1, arg2, block);
    }

    @Override
    protected boolean methodMissing(DynamicMethod method, IRubyObject caller) {
        return method.isUndefined() || (!methodName.equals("method_missing") && !method.isCallableFrom(caller, callType));
    }

    @Override
    public IRubyObject callIter(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject[] args, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            try {
                return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, args, block);
            } catch (JumpException.BreakJump bj) {
                return handleBreakJump(context, bj);
            } catch (JumpException.RetryJump rj) {
                throw retryJumpError(context);
            } finally {
                block.escape();
            }
        }

        return super.callIter(context, caller, self, args, block);
    }

    @Override
    public IRubyObject callVarargsIter(ThreadContext context, IRubyObject caller, IRubyObject self, IRubyObject[] args, Block block) {
        CacheEntry refinedMethod = findRefinedMethod(context.runtime, self, methodName);

        if (!refinedMethod.method.isUndefined()) {
            try {
                return refinedMethod.method.call(context, self, self.getMetaClass(), methodName, args, block);
            } catch (JumpException.BreakJump bj) {
                return handleBreakJump(context, bj);
            } catch (JumpException.RetryJump rj) {
                throw retryJumpError(context);
            } finally {
                block.escape();
            }
        }

        return super.callIter(context, caller, self, args, block);
    }
}
