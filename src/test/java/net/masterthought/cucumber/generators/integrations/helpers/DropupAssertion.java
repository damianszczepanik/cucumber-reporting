package net.masterthought.cucumber.generators.integrations.helpers;

import jdk.nashorn.internal.codegen.CompilerConstants;

/**
 * @author Sean Bucholtz (sean.bucholtz@gmail.com)
 */
public class DropupAssertion extends WebAssertion {
    public CalloutAssertion[] getCallouts() {
        return allByClass("bs-callout bs-callout-danger", CalloutAssertion.class);
    }
}
