package rules;

import tcore.Iterator;
import tcore.LHS;
import tcore.Matcher;
import tcore.RHS;
import tcore.messages.Packet;

/**
 * Applies the transformation on all matches found.
 *
 * @author Pierre-Olivier Talbot
 */
public class FRule extends ARule {


    FRule(String name, LHS lhs, RHS rhs, boolean withResolver) {
        super(name, lhs, rhs, withResolver);

        matcher = new Matcher(lhs, Integer.MAX_VALUE);
        iterator = new Iterator(Integer.MAX_VALUE);
    }

    @Override
    public Packet packetIn(Packet p) {

        p = matcher.packetIn(p);
        if (checkModuleForFailure(matcher)) return p;

        while (!p.getCurrentMatchSet().getMatches().isEmpty()) {
            p = iterator.packetIn(p);
            if (checkModuleForFailure(iterator)) return p;

            p = rewriter.packetIn(p);
            if (checkModuleForFailure(rewriter)) return p;

            p = resolveOrClean(p);
        }

        isSuccess = true;
        return p;
    }
}
