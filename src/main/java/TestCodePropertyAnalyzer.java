import instrumentor.JSASTInstrumentor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.CopyUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.Alert;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;

import core.JSAnalyzer;
import core.TraceAnalyzer;

public class TestCodePropertyAnalyzer {

	private static String testsFolder = "/Users/aminmf/Documents/JavaScriptTestsStudy/popularJS/jquery-mobile/tests/";
	private static String[] excludeFolders = { "casperjs"};		
	private static String testsFramework = "qunit";  // {"qunit", "jasmine", "mocha"}
	

	private static ArrayList<String> jsFileNames = new ArrayList<String>();
	private static JSAnalyzer codeAnalyzer;
	private static TraceAnalyzer traceAnalyzer = new TraceAnalyzer();
	private static int NumTests;
	private static int NumAsyncTests;
	private static int NumAssertions;
	private static int MaxFunCall;
	private static float AveFunCall;
	private static int NumDOMFixture;
	private static int NumTriggerTest;
	private static int NumObjCreate;

	public static void main(String[] args) throws Exception {

		codeAnalyzer = new JSAnalyzer(new JSASTInstrumentor(), testsFolder, null);		

		// For each .js test file
		System.out.println("Test framework: " + testsFramework);
		File[] files = new File(testsFolder).listFiles();
		if (files==null){
			System.out.println("No file found in directory: " + testsFolder);
			return;
		}
		for (File file : files)
			processFile(file);


		
		/*
		// Collect execution traces
		ArrayList traceList = null;
		HashMultimap<String, String> functionCallsMultiMap = HashMultimap.create();
		for (String jsFile: jsFileNames){
			System.out.println("Getting the traceList from " + jsFile);
			//System.out.println("return " + jsFile.replace(".js","") + "_getFuncionCallTrace();");
			//traceList = (ArrayList)((JavascriptExecutor) driver).executeScript("return " + jsFile.replace(".js","").replace("-", "_") + "_getFuncionCallTrace();");
			System.out.println("traceList: " + traceList);
			Map<String,String> traceMap;
			for (int i=0; i<traceList.size(); i++){
				traceMap = (Map<String,String>)(traceList.get(i));
				String testFunction = traceMap.get("testFunction");
				String calledFunctionName = traceMap.get("functionName");
				functionCallsMultiMap.put(testFunction, calledFunctionName);
			}
		}

		int numUniqueFunCalls = 0;
		int maxUniqueFunCalls = 0;
		for (String testFunc : functionCallsMultiMap.keySet()) {
			Set<String> calledFunc = functionCallsMultiMap.get(testFunc);
			System.out.println(testFunc + ": " + calledFunc);
			if (calledFunc.size() > maxUniqueFunCalls)
				maxUniqueFunCalls = calledFunc.size(); 
			numUniqueFunCalls += calledFunc.size();
		}

		System.out.println("numUniqueFunCalls: " + numUniqueFunCalls);
		if (functionCallsMultiMap.keySet().size()!=0) 
			System.out.println("aveUniqueFunCalls: " + numUniqueFunCalls/functionCallsMultiMap.keySet().size());
		System.out.println("maxUniqueFunCalls: " + maxUniqueFunCalls);
		*/
	}

	private static void processFile(File file) throws IOException, Exception {
		if (ArrayUtils.contains(excludeFolders, file.getName())){
			System.out.println("*** Analysis excluded for: " + file.getName());
			return;
		}
		if (file.isDirectory()){
			System.out.println("*** Analysing directory: " + file.getAbsolutePath().replace(testsFolder, ""));
			File[] files = file.listFiles();
			if (files==null){
				System.out.println("No test file found in directory: " + file.getAbsolutePath().replace(testsFolder, ""));
				return;
			}
			for (File innerFile : files)
				processFile(innerFile);
		}
		if (file.isFile()) {
			String fileName = file.getName();
			//if (fileName.endsWith(".js")){
			if (!fileName.contains("qunit") && fileName.endsWith(".js")) {
				//&& !fileName.equals("es.js") && !fileName.equals("helpers.js") && !fileName.equals("karma.conf.js") && !fileName.equals("es.js") && !fileName.equals("library.js")
				//){
				analyseJSTestFile(file.getCanonicalPath());
			}
		}
		
	}
	
	

	private static void analyseJSTestFile(String canonicalPath) throws Exception {
		/*
	 	NumTests: Number of tests
		NumAsyncTests: Number of async tests
		NumAssertions: Number of assertions
		MaxFunCall: Maximum number of unique function calls per test
		AveFunCall: Average number of unique function calls per test
		NumDOMFixture: Number of DOM fixtures in the test suite
		NumTriggerTest: Number of tests with event triggering methods
		NumObjCreate: Number of objects creation in the test suite
		*/
		File jsFile = new File(canonicalPath);
		String fileName = jsFile.getName();

		System.out.println(canonicalPath);
		codeAnalyzer.setJSFileName(fileName);
		codeAnalyzer.setJSAddress(canonicalPath);
		codeAnalyzer.setTestFramework(testsFramework);
		//codeAnalyzer.setJSAddress(testsFolder + "/" + fileName);
		System.out.println("Analysing the test suite in file " + fileName);
		codeAnalyzer.analyzeTestCodeProperties();
		
		
		NumTests += codeAnalyzer.getNumTests();
		NumAsyncTests += codeAnalyzer.getNumAsyncTests();
		NumAssertions += codeAnalyzer.getNumAssertions();
		MaxFunCall += codeAnalyzer.getMaxFunCall();
		AveFunCall += codeAnalyzer.getAveFunCall();
		NumDOMFixture += codeAnalyzer.getNumDOMFixture();
		NumTriggerTest += codeAnalyzer.getNumTriggerTest();
		NumObjCreate += codeAnalyzer.getNumObjCreate();
		
		
		System.out.println("==========================");
		System.out.println("++++ NumTests: " + NumTests);
		System.out.println("++++ NumAsyncTests: " + NumAsyncTests);
		System.out.println("++++ NumAssertions: " + NumAssertions);
		System.out.println("++++ MaxFunCall: " + MaxFunCall);
		System.out.println("++++ AveFunCall: " + AveFunCall);
		System.out.println("++++ NumDOMFixture: " + NumDOMFixture);
		System.out.println("++++ NumTriggerTest: " + NumTriggerTest);
		System.out.println("++++ NumObjCreate: " + NumObjCreate);
		System.out.println("==========================");
		
	}

	
}

