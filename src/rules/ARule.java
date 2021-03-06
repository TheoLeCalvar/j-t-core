package rules;

import tcore.*;
import tcore.messages.Packet;

/**
 * Applies the transformation on a single match.
 *
 * @author Pierre-Olivier Talbot
 */
public class ARule extends BasicRule {

    ARule(String name, LHS lhs, RHS rhs, boolean withResolver) {
        this.name = name;
        this.lhs = lhs;
        this.rhs = rhs;
        matcher = new Matcher(lhs, 1);
        iterator = new Iterator(1);
        rewriter = new Rewriter(rhs);
        if (withResolver) resolver = new Resolver(false, null);
    }

    @Override
    public Packet packetIn(Packet p) {

        p = matcher.packetIn(p);
        if (checkModuleForFailure(matcher)) return p;

        p = iterator.packetIn(p);
        if (checkModuleForFailure(iterator)) return p;

        p = rewriter.packetIn(p);
        if (checkModuleForFailure(rewriter)) return p;

        p = resolveOrClean(p);

        isSuccess = true;
        return p;
    }

    @Override
    public Packet nextIn(Packet p) {
        return packetIn(p);
    }

    protected Packet resolveOrClean(Packet p) {
        if (resolver != null) {
            p = resolver.packetIn(p);
            if (checkModuleForFailure(rewriter)) return p;
        } else {
            p.clean();
        }
        return p;
    }

}
