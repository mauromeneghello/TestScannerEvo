package core;

import instrumentor.JSASTInstrumentor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.bcel.generic.FLOAD;
import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.mozilla.javascript.CompilerEnvirons;
import org.mozilla.javascript.Context;
import org.mozilla.javascript.Parser;
import org.mozilla.javascript.RhinoException;
import org.mozilla.javascript.ast.AstRoot;



public class JSAnalyzer {

	private List<String> excludeFilenamePatterns;

	private JSASTInstrumentor astVisitor;
	private String outputfolder;
	private String jsAddress, scopeName;

	public void setJSAddress(String jsAddress){
		this.jsAddress = jsAddress;
	}

	public void setJSFileName(String scopeName){
		this.scopeName = scopeName;
	}

	/**
	 * Construct without patterns.
	 * 
	 * @param astVisit
	 *            The JSASTVisitor to run over all JavaScript.
	 * @param scopeName 
	 * @param jsAddress 
	 */
	public JSAnalyzer(JSASTInstrumentor astVisit, String jsAddress, String scopeName) {
		this.excludeFilenamePatterns = new ArrayList<String>();
		this.astVisitor = astVisit;
		this.jsAddress = jsAddress;
		this.scopeName = scopeName;
	}

	/**
	 * Constructor with patterns.
	 * 
	 * @param astVisit
	 *            The JSASTVisitor to run over all JavaScript.
	 * @param excludes
	 *            List with variable patterns to exclude.
	 */
	public JSAnalyzer(JSASTInstrumentor astVisit, List<String> excludes) {
		excludeFilenamePatterns = excludes;
		astVisitor = astVisit;
	}

	public JSAnalyzer(String outputfolder){
		excludeFilenamePatterns = new ArrayList<String>();
		this.outputfolder=outputfolder;
	}


	/**
	 * @param jsAddress
	 *            Address of the JavaScript code to be instrumented
	 * @param scopeName
	 *            Name of the current scope (filename mostly)
	 * @throws Exception 
	 */
	@SuppressWarnings("deprecation")
	public String instrumentJavaScript() throws Exception {

		// reading js form the input file
		String input = "";
		FileInputStream inputStream = new FileInputStream(jsAddress);
		String outputFileAddress = jsAddress.replace(".js", "_instrumented.js");
		FileOutputStream outputStream = new FileOutputStream(outputFileAddress);
		try {
			input = IOUtils.toString(inputStream);
		} finally {
			inputStream.close();
		}	    

		try {
			AstRoot ast = null;	

			/* initialize JavaScript context */
			Context cx = Context.enter();

			/* create a new parser */
			Parser rhinoParser = new Parser(new CompilerEnvirons(), cx.getErrorReporter());

			/* parse some script and save it in AST */
			ast = rhinoParser.parse(new String(input), scopeName, 0);

			//System.out.println("AST BEFORE INSTRUMENTATION: ");
			//System.out.println(ast.toSource());

			//System.out.println(ast.debugPrint());

			//writeJSToFile(scopename, input);
			//writeFunctionsToFile(input);

			//System.out.println("AST BEFORE : ");
			//System.out.println(ast.toSource());

			astVisitor.resetUnitTestCounter();

			astVisitor.setScopeName(scopeName);


			astVisitor.start();

			//astVisitor.setInstrumentFunctionCall(true);
			/* recurse through AST */
			ast.visit(astVisitor);


			//System.out.println("DONE WITH FUNCTION CALLS");
			//astVisitor.setInstrumentFunctionCall(false);
			//ast.visit(astVisitor);

			astVisitor.finish(ast);

			System.out.println("assertionCounter: " + astVisitor.getAssertionCounter());
			System.out.println("newExpressionCounter: " + astVisitor.getNewExpressionCounter());
			System.out.println("testCounter: " + astVisitor.getTestCounter());
			System.out.println("asynchTestCounter: " + astVisitor.getAsynchTestCounter());
			System.out.println("trieggerCounter: " + astVisitor.getTriggerCounetr());

			/* clean up */
			Context.exit();

			//System.out.println("AST AFTER INSTRUMENTATION: ");
			String instrumentedCode = ast.toSource();
			//System.out.println(instrumentedCode);
			//System.out.println(ast.debugPrint());

			try {
				CopyUtils.copy( instrumentedCode, outputStream);
				outputStream.flush();
			} finally {
				outputStream.close();
			}

			return ast.toSource();
		} catch (RhinoException re) {
			System.err.println(re.getMessage());
			System.out.println("Unable to instrument. This might be a JSON response sent"
					+ " with the wrong Content-Type or a syntax error.");
		} catch (IllegalArgumentException iae) {

			System.out.println("Invalid operator exception catched. Not instrumenting code.");
		}
		System.out.println("Here is the corresponding buffer: \n" + input + "\n");

		return input;
	}



	private void writeJSToFile(String scopename, String input) {
		try {
			System.out.println("writing on /jsCode/" + scopename);
			File file = new File("jsCode/" + scopename);
			if (!file.exists()) {
				file.createNewFile();
			}
			FileOutputStream fop = new FileOutputStream(file);
			fop.write(input.getBytes());
			fop.flush();
			fop.close();
		}
		catch (IOException ioe) {
			System.out.println("IO Exception");
		}
	}

	// Look for instances of "function" in input then figure out where it ends
	private void writeFunctionsToFile(String input) {
		String inputCopy = input;
		int indexOfFuncString = inputCopy.indexOf("function ");
		while (indexOfFuncString != -1) {
			String sub = inputCopy.substring(indexOfFuncString);
			int nextOpenParen = sub.indexOf("(");
			String funcName = sub.substring(9, nextOpenParen); //"function " has 9 characters
			int firstOpenBrace = sub.indexOf("{");
			int countOpenBraces = 1;
			int countCloseBraces = 0;
			int endIndex = firstOpenBrace;
			while (countOpenBraces != countCloseBraces) {
				endIndex++;
				if (sub.charAt(endIndex) == '{') {
					countOpenBraces++;
				}
				else if (sub.charAt(endIndex) == '}') {
					countCloseBraces++;
				}
			}
			String code = sub.substring(0, endIndex+1);
			try {
				File file = new File("jsCode/" +  funcName + ".js");
				if (!file.exists()) {
					file.createNewFile();
				}
				FileOutputStream fop = new FileOutputStream(file);
				fop.write(code.getBytes());
				fop.flush();
				fop.close();
			}
			catch (IOException ioe) {
				System.out.println("IO Exception");
			}
			inputCopy = sub.substring(endIndex+1);
			indexOfFuncString = inputCopy.indexOf("function ");
		}
	}


	public void analyze(ArrayList<Integer> coveredLines, ArrayList<Integer> missedLines, ArrayList<Integer> coveredFunctionsIndices, ArrayList<Integer> missedFunctionsIndices) throws Exception {

		astVisitor.setInstrumentationEnable(false);
		astVisitor.setFunctionCounter(0); // resetting the index of visited Function nodes for annotating covered functions
		astVisitor.clearFunctionsList(); // clearing list of covered and missed function from previous visit

		// reading js form the input file
		String input = "";
		FileInputStream inputStream = new FileInputStream(jsAddress);
		try {
			input = IOUtils.toString(inputStream);
		} finally {
			inputStream.close();
		}	    

		try {
			AstRoot ast = null;	
			/* initialize JavaScript context */
			Context cx = Context.enter();
			/* create a new parser */
			Parser rhinoParser = new Parser(new CompilerEnvirons(), cx.getErrorReporter());
			/* parse some script and save it in AST */
			ast = rhinoParser.parse(new String(input), scopeName, 0);

			//System.out.println("************** AST ******************");
			//System.out.println(ast.toSource());
			//System.out.println(ast.debugPrint());
			//writeJSToFile(scopename, input);
			//writeFunctionsToFile(input);
			//System.out.println("AST BEFORE : ");
			//System.out.println(ast.toSource());

			astVisitor.setScopeName(scopeName);
			astVisitor.setCoverageInfo(coveredLines, missedLines, coveredFunctionsIndices, missedFunctionsIndices);
			/* recurse through AST */
			astVisitor.setVisitOnly("FunctionNode");
			ast.visit(astVisitor);

			System.out.println("CoveredFunctions :" + astVisitor.getCoveredFunctions());
			//System.out.println("CoveredFunctions.size() :" + astVisitor.getCoveredFunctions().size());
			System.out.println("CoveredFunctionLines :" + astVisitor.getCoveredFunctionLines());
			System.out.println("MissedFunctions :" + astVisitor.getMissedFunctions());
			//System.out.println("MissedFunctions.size() :" + astVisitor.getMissedFunctions().size());
			System.out.println("MissedFunctionLines :" + astVisitor.getMissedFunctionLines());

			astVisitor.setVisitOnly("FunctionCall");
			ast.visit(astVisitor);

			System.out.println("FunctionCalls :" + astVisitor.getFunctionCalls());

			for (String functionCall : astVisitor.getFunctionCalls()){
				if (functionCall.contains(".call") || functionCall.contains(".apply"))   // The call() and apply() methods calls a function with a given this value and arguments
					functionCall = functionCall.replace(".call", "").replace(".apply", "");
				if (astVisitor.getMissedFunctions().contains(functionCall))
					System.out.println("The call to function " + functionCall + " was never executed!");
			}

			ArrayList<Integer> msimf = astVisitor.getMissedStatementInMissedFunction();
			System.out.println("msimf: " + msimf);
			int totalMissedStatementLinesInMissedFunctionCounter = 0;
			for (int i=0; i<msimf.size(); i++){
				if (msimf.get(i) >= 0)
					totalMissedStatementLinesInMissedFunctionCounter++;
			}

			System.out.println("@ Total missed statement lines in missed functioncounter = " + totalMissedStatementLinesInMissedFunctionCounter);
			System.out.println("@ Total number of missed statements = " + astVisitor.getMissedStatementLines().size());
			float ratio = (float)totalMissedStatementLinesInMissedFunctionCounter/(float)astVisitor.getMissedStatementLines().size();
			if (astVisitor.getMissedStatementLines().size()!=0)
				System.out.println("@ Ratio of total missed statement lines = " + ratio);


			/*
			System.out.println("assertionCounter: " + astVisitor.getAssertionCounter());
			System.out.println("newExpressionCounter: " + astVisitor.getNewExpressionCounter());
			System.out.println("testCounter: " + astVisitor.getTestCounter());
			System.out.println("asynchTestCounter: " + astVisitor.getAsynchTestCounter());
			System.out.println("trieggerCounter: " + astVisitor.getTriggerCounetr());
			 */

			/* clean up */
			Context.exit();
		} catch (RhinoException re) {
			System.err.println(re.getMessage());
			System.out.println("Unable to instrument. This might be a JSON response sent"
					+ " with the wrong Content-Type or a syntax error.");
		} catch (IllegalArgumentException iae) {

			System.out.println("Invalid operator exception catched. Not instrumenting code.");
		}

		//System.out.println("Here is the corresponding buffer: \n" + input + "\n");

		astVisitor.setInstrumentationEnable(true);

	}

}
