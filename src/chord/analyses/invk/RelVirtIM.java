/*
 * Copyright (c) 2008-2010, Intel Corporation.
 * Copyright (c) 2006-2007, The Trustees of Stanford University.
 * All rights reserved.
 * Licensed under the terms of the New BSD License.
 */
package chord.analyses.invk;

import joeq.Class.jq_Method;
import joeq.Compiler.Quad.Operator;
import joeq.Compiler.Quad.Quad;
import joeq.Compiler.Quad.Operand.MethodOperand;
import joeq.Compiler.Quad.Operator.Invoke;
import joeq.Compiler.Quad.Operator.Invoke.InvokeInterface;
import joeq.Compiler.Quad.Operator.Invoke.InvokeVirtual;
import chord.analyses.method.DomM;
import chord.project.Messages;
import chord.project.Chord;
import chord.project.Config;
import chord.project.analyses.ProgramRel;

/**
 * Relation containing each tuple (i,m) such that m is the resolved
 * method of method invocation statement i of kind
 * <tt>INVK_VIRTUAL</tt> or <tt>INVK_INTERFACE</tt>.
 *
 * @author Mayur Naik (mhn@cs.stanford.edu)
 */
@Chord(
	name = "virtIM",
	sign = "I0,M0:I0xM0"
)
public class RelVirtIM extends ProgramRel {
	private final static String NOT_FOUND =
		"WARN: RelVirtIM: Target method %s of call site %s not found in domain M.";
	public void fill() {
		DomI domI = (DomI) doms[0];
		DomM domM = (DomM) doms[1];
		int numI = domI.size();
		for (int iIdx = 0; iIdx < numI; iIdx++) {
			Quad i = domI.get(iIdx);
			Operator op = i.getOperator();
			if (op instanceof InvokeVirtual || op instanceof InvokeInterface) {
				jq_Method m = Invoke.getMethod(i).getMethod();
				int mIdx = domM.indexOf(m);
				if (mIdx >= 0) 
					add(iIdx, mIdx);
				else if (Config.verbose >= 2)
					Messages.log(NOT_FOUND, m, i.toLocStr());
			}
		}
	}
}
