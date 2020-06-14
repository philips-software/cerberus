package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsDiffResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsMethodResult;

import java.util.*;
import java.util.stream.Stream;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.PATH_SEPARATOR;

public class CodeMetricsDiffService {

    private String previousPath;
    private String currentPath;

    public CodeMetricsDiffService(String previousPath, String currentPath) {
        this.previousPath = previousPath;
        this.currentPath = currentPath;
    }

    public List<CodeMetricsClassResult> getMetricsFromSourceCode() {
        List<CodeMetricsClassResult> codeMetricsClassResultsList = new ArrayList<CodeMetricsClassResult>();
        new CK().calculate(currentPath, false, result -> {
            CodeMetricsClassResult codeMetricsClassResult = new CodeMetricsClassResult();
            transformResults(result, codeMetricsClassResult, "new");
            codeMetricsClassResultsList.add(codeMetricsClassResult);
        });

        new CK().calculate(previousPath, false, result -> {
            Optional<CodeMetricsClassResult> existingNewResults = codeMetricsClassResultsList
                    .stream()
                    .filter(metrics -> metrics.getFile().contains((result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1))))
                    .findFirst();
            if(existingNewResults.isPresent()) {
                transformResults(result, existingNewResults.get(), "old");
            } else {
                CodeMetricsClassResult codeMetricsClassResult = new CodeMetricsClassResult();
                transformResults(result, codeMetricsClassResult, "old");
                codeMetricsClassResultsList.add(codeMetricsClassResult);
            }
        });

        return codeMetricsClassResultsList;
    }

    private void transformResults(CKClassResult result, CodeMetricsClassResult codeMetricsClassResult, String valueType) {
        transFormClassResult(result, codeMetricsClassResult, valueType);
        transFormMethodResult(result, codeMetricsClassResult, valueType);
    }

    private void transFormMethodResult(CKClassResult result, CodeMetricsClassResult codeMetricsClassResult, String valueType) {
        Set<CKMethodResult> setOfMethodResults = result.getMethods();
        List<CodeMetricsMethodResult> listOfMethodMetricsOfClass = new ArrayList<CodeMetricsMethodResult>();
        setOfMethodResults.stream().forEach(methodResult -> {
            CodeMetricsMethodResult codeMetricsMethodResult = getMethodMetricsResultForTransform(codeMetricsClassResult, methodResult);
            setupMethodMetrics(result, valueType, methodResult, codeMetricsMethodResult);
            listOfMethodMetricsOfClass.add(codeMetricsMethodResult);
        });
        codeMetricsClassResult.setMethodMetrics(listOfMethodMetricsOfClass);
    }

    private CodeMetricsMethodResult getMethodMetricsResultForTransform(CodeMetricsClassResult codeMetricsClassResult, CKMethodResult ckMethodResult ) {
        Optional<CodeMetricsMethodResult> existingMethodMetrics = Optional.ofNullable(codeMetricsClassResult.getMethodMetrics()).map(Collection::stream).orElseGet(Stream::empty)
                .filter(methodMetrics -> methodMetrics.getMethodName().contains(ckMethodResult.getMethodName())).findFirst();
        if(existingMethodMetrics.isPresent()) {
            return existingMethodMetrics.get();
        } else {
            return new CodeMetricsMethodResult();
        }
    }

    private void setupMethodMetrics(CKClassResult result, String valueType, CKMethodResult methodResult, CodeMetricsMethodResult codeMetricsMethodResult) {
        String filename = result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1);
        String constructType = "METHOD";
        String className = result.getClassName();
        String constructName = className + "::" + methodResult.getMethodName();
        if(methodResult.isConstructor()) {
            constructName = className + ":CONSTRUCTOR:" + methodResult.getMethodName();
        }
        codeMetricsMethodResult.setMethodName(constructName);
        codeMetricsMethodResult.setType(constructType);
        codeMetricsMethodResult.setFile(filename);
        codeMetricsMethodResult.setComplexity(getCodeMetricsResultWithValue(methodResult.getWmc(), valueType, codeMetricsMethodResult.getComplexity(), "COMPLEXITY_OF_METHOD", constructName, constructType, filename));
        codeMetricsMethodResult.setParametersQty(getCodeMetricsResultWithValue(methodResult.getParametersQty(), valueType, codeMetricsMethodResult.getParametersQty(), "NO_OF_PARAMETERS", constructName, constructType, filename));
        codeMetricsMethodResult.setLinesOfCode(getCodeMetricsResultWithValue(methodResult.getLoc(), valueType, codeMetricsMethodResult.getLinesOfCode(), "LINES_OF_CODE", constructName, constructType, filename));
        codeMetricsMethodResult.setStartLineNo(getCodeMetricsResultWithValue(methodResult.getStartLine(), valueType, codeMetricsMethodResult.getStartLineNo(), "START_LINE_NO", constructName, constructType, filename));
        codeMetricsMethodResult.setComparisonsQty(getCodeMetricsResultWithValue(methodResult.getComparisonsQty(), valueType, codeMetricsMethodResult.getComparisonsQty(), "NO_OF_COMPARISONS", constructName, constructType, filename));
        codeMetricsMethodResult.setTryCatchQty(getCodeMetricsResultWithValue(methodResult.getTryCatchQty(), valueType, codeMetricsMethodResult.getTryCatchQty(), "NO_OF_TRYCATCH_BLOCKS", constructName, constructType, filename));
        codeMetricsMethodResult.setParenthesizedExpsQty(getCodeMetricsResultWithValue(methodResult.getParenthesizedExpsQty(), valueType, codeMetricsMethodResult.getParenthesizedExpsQty(), "NO_OF_PARENTHESIZED_EXPRESSIONS", constructName, constructType, filename));
        codeMetricsMethodResult.setStringLiteralsQty(getCodeMetricsResultWithValue(methodResult.getStringLiteralsQty(), valueType, codeMetricsMethodResult.getStringLiteralsQty(), "NO_OF_STRING_LITERALS", constructName, constructType, filename));
        codeMetricsMethodResult.setNumbersQty(getCodeMetricsResultWithValue(methodResult.getNumbersQty(), valueType, codeMetricsMethodResult.getNumbersQty(), "NO_OF_NOS", constructName, constructType, filename));
        codeMetricsMethodResult.setAssignmentsQty(getCodeMetricsResultWithValue(methodResult.getAssignmentsQty(), valueType, codeMetricsMethodResult.getAssignmentsQty(), "NO_OF_ASSIGNMENTS", constructName, constructType, filename));
        codeMetricsMethodResult.setMathOperationsQty(getCodeMetricsResultWithValue(methodResult.getMathOperationsQty(), valueType, codeMetricsMethodResult.getMathOperationsQty(), "NO_OF_MATH_OPERATIONS", constructName, constructType, filename));
        codeMetricsMethodResult.setVariablesQty(getCodeMetricsResultWithValue(methodResult.getVariablesQty(), valueType, codeMetricsMethodResult.getVariablesQty(), "NO_OF_VARIABLES", constructName, constructType, filename));
        codeMetricsMethodResult.setMaxNestedBlocks(getCodeMetricsResultWithValue(methodResult.getMaxNestedBlocks(), valueType, codeMetricsMethodResult.getMaxNestedBlocks(), "NO_OF_MAX_NESTED_BLOCKS", constructName, constructType, filename));
        codeMetricsMethodResult.setAnonymousClassesQty(getCodeMetricsResultWithValue(methodResult.getAnonymousClassesQty(), valueType, codeMetricsMethodResult.getAnonymousClassesQty(), "NO_OF_ANONYMOUS_CLASSES", constructName, constructType, filename));
        codeMetricsMethodResult.setSubClassesQty(getCodeMetricsResultWithValue(methodResult.getSubClassesQty(), valueType, codeMetricsMethodResult.getSubClassesQty(), "NO_OF_SUBCLASSES", constructName, constructType, filename));
        codeMetricsMethodResult.setLambdasQty(getCodeMetricsResultWithValue(methodResult.getLambdasQty(), valueType, codeMetricsMethodResult.getLambdasQty(), "NO_OF_LAMBDAS", constructName, constructType, filename));
        codeMetricsMethodResult.setLoopQty(getCodeMetricsResultWithValue(methodResult.getLoopQty(), valueType, codeMetricsMethodResult.getLoopQty(), "NO_OF_LOOPS", constructName, constructType, filename));
        codeMetricsMethodResult.setUniqueWordsQty(getCodeMetricsResultWithValue(methodResult.getUniqueWordsQty(), valueType, codeMetricsMethodResult.getUniqueWordsQty(), "NO_OF_UNIQUE_WORDS", constructName, constructType, filename));
    }

    private void transFormClassResult(CKClassResult result, CodeMetricsClassResult codeMetricsClassResult, String valueType) {
        String filename = result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1);
        String constructName = result.getClassName();
        String constructType = result.getType().toUpperCase();
        codeMetricsClassResult.setFile(filename);
        codeMetricsClassResult.setClassName(constructName);
        codeMetricsClassResult.setType(constructType);
        codeMetricsClassResult.setDepthInheritanceTree(getCodeMetricsResultWithValue(result.getDit(), valueType, codeMetricsClassResult.getDepthInheritanceTree(), "DEPTH_INHERITANCE_TREE", constructName, constructType, filename));
        codeMetricsClassResult.setWeightMethodClass(getCodeMetricsResultWithValue(result.getWmc(), valueType, codeMetricsClassResult.getWeightMethodClass(), "WEIGHT_METHOD_CLASS", constructName, constructType, filename));
        codeMetricsClassResult.setCouplingBetweenObjects(getCodeMetricsResultWithValue(result.getCbo(), valueType, codeMetricsClassResult.getCouplingBetweenObjects(), "COUPLING_BETWEEN_OBJECTS", constructName, constructType, filename));
        codeMetricsClassResult.setResponseForClass(getCodeMetricsResultWithValue(result.getRfc(), valueType, codeMetricsClassResult.getResponseForClass(), "RESPONSE_FOR_A_CLASS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfStaticInvocations(getCodeMetricsResultWithValue(result.getNosi(), valueType, codeMetricsClassResult.getNumberOfStaticInvocations(), "NO_OF_STATIC_INVOCATIONS", constructName, constructType, filename));
        codeMetricsClassResult.setLinesOfCode(getCodeMetricsResultWithValue(result.getLoc(), valueType, codeMetricsClassResult.getLinesOfCode(), "LINES_OF_CODE", constructName, constructType, filename));
        codeMetricsClassResult.setReturnQty(getCodeMetricsResultWithValue(result.getReturnQty(), valueType, codeMetricsClassResult.getReturnQty(), "NO_OF_RETURNS", constructName, constructType, filename));
        codeMetricsClassResult.setLoopQty(getCodeMetricsResultWithValue(result.getLoopQty(), valueType, codeMetricsClassResult.getLoopQty(), "NO_OF_LOOPS", constructName, constructType, filename));
        codeMetricsClassResult.setComparisonsQty(getCodeMetricsResultWithValue(result.getComparisonsQty(), valueType, codeMetricsClassResult.getComparisonsQty(), "NO_OF_COMPARISONS", constructName, constructType, filename));
        codeMetricsClassResult.setTryCatchQty(getCodeMetricsResultWithValue(result.getTryCatchQty(), valueType, codeMetricsClassResult.getTryCatchQty(), "NO_OF_TRYCATCH_BLOCKS", constructName, constructType, filename));
        codeMetricsClassResult.setParenthesizedExpsQty(getCodeMetricsResultWithValue(result.getParenthesizedExpsQty(), valueType, codeMetricsClassResult.getParenthesizedExpsQty(), "NO_OF_PARENTHESIZED_EXPRESSIONS", constructName, constructType, filename));
        codeMetricsClassResult.setStringLiteralsQty(getCodeMetricsResultWithValue(result.getStringLiteralsQty(), valueType, codeMetricsClassResult.getStringLiteralsQty(), "NO_OF_STRING_LITERALS", constructName, constructType, filename));
        codeMetricsClassResult.setNumbersQty(getCodeMetricsResultWithValue(result.getNumbersQty(), valueType, codeMetricsClassResult.getNumbersQty(), "NO_OF_NOS", constructName, constructType, filename));
        codeMetricsClassResult.setAssignmentsQty(getCodeMetricsResultWithValue(result.getAssignmentsQty(), valueType, codeMetricsClassResult.getAssignmentsQty(), "NO_OF_ASSIGNMENTS", constructName, constructType, filename));
        codeMetricsClassResult.setMathOperationsQty(getCodeMetricsResultWithValue(result.getMathOperationsQty(), valueType, codeMetricsClassResult.getMathOperationsQty(), "NO_OF_MATH_OPERATIONS", constructName, constructType, filename));
        codeMetricsClassResult.setVariablesQty(getCodeMetricsResultWithValue(result.getVariablesQty(), valueType, codeMetricsClassResult.getVariablesQty(), "NO_OF_VARIABLES", constructName, constructType, filename));
        codeMetricsClassResult.setMaxNestedBlocks(getCodeMetricsResultWithValue(result.getMaxNestedBlocks(), valueType, codeMetricsClassResult.getMaxNestedBlocks(), "NO_OF_MAX_NESTED_BLOCKS", constructName, constructType, filename));
        codeMetricsClassResult.setAnonymousClassesQty(getCodeMetricsResultWithValue(result.getAnonymousClassesQty(), valueType, codeMetricsClassResult.getAnonymousClassesQty(), "NO_OF_ANONYMOUS_CLASSES", constructName, constructType, filename));
        codeMetricsClassResult.setSubClassesQty(getCodeMetricsResultWithValue(result.getSubClassesQty(), valueType, codeMetricsClassResult.getSubClassesQty(), "NO_OF_SUBCLASSES", constructName, constructType, filename));
        codeMetricsClassResult.setLambdasQty(getCodeMetricsResultWithValue(result.getLambdasQty(), valueType, codeMetricsClassResult.getLambdasQty(), "NO_OF_LAMBDAS", constructName, constructType, filename));
        codeMetricsClassResult.setUniqueWordsQty(getCodeMetricsResultWithValue(result.getUniqueWordsQty(), valueType, codeMetricsClassResult.getUniqueWordsQty(), "NO_OF_UNIQUE_WORDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfMethods(getCodeMetricsResultWithValue(result.getNumberOfMethods(), valueType, codeMetricsClassResult.getNumberOfMethods(), "NO_OF_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfStaticMethods(getCodeMetricsResultWithValue(result.getNumberOfStaticMethods(), valueType, codeMetricsClassResult.getNumberOfStaticMethods(), "NO_OF_STATIC_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfPublicMethods(getCodeMetricsResultWithValue(result.getNumberOfPublicMethods(), valueType, codeMetricsClassResult.getNumberOfPublicMethods(), "NO_OF_PUBLIC_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfPrivateMethods(getCodeMetricsResultWithValue(result.getNumberOfPrivateMethods(), valueType, codeMetricsClassResult.getNumberOfPrivateMethods(), "NO_OF_PRIVATE_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfProtectedMethods(getCodeMetricsResultWithValue(result.getNumberOfProtectedMethods(), valueType, codeMetricsClassResult.getNumberOfProtectedMethods(), "NO_OF_PROTECTED_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfDefaultMethods(getCodeMetricsResultWithValue(result.getNumberOfDefaultMethods(), valueType, codeMetricsClassResult.getNumberOfDefaultMethods(), "NO_OF_DEFAULT_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfAbstractMethods(getCodeMetricsResultWithValue(result.getNumberOfAbstractMethods(), valueType, codeMetricsClassResult.getNumberOfAbstractMethods(), "NO_OF_ABSTRACT_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfFinalMethods(getCodeMetricsResultWithValue(result.getNumberOfFinalMethods(), valueType, codeMetricsClassResult.getNumberOfFinalFields(), "NO_OF_FINAL_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfSynchronizedMethods(getCodeMetricsResultWithValue(result.getNumberOfSynchronizedMethods(), valueType, codeMetricsClassResult.getNumberOfSynchronizedFields(), "NO_OF_SYNCHRONIZED_METHODS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfFields(getCodeMetricsResultWithValue(result.getNumberOfFields(), valueType, codeMetricsClassResult.getNumberOfFields(), "NO_OF_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfStaticFields(getCodeMetricsResultWithValue(result.getNumberOfStaticFields(), valueType, codeMetricsClassResult.getNumberOfStaticFields(), "NO_OF_STATIC_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfPublicFields(getCodeMetricsResultWithValue(result.getNumberOfPublicFields(), valueType, codeMetricsClassResult.getNumberOfPublicFields(), "NO_OF_PUBLIC_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfPrivateFields(getCodeMetricsResultWithValue(result.getNumberOfPrivateFields(), valueType, codeMetricsClassResult.getNumberOfPrivateFields(), "NO_OF_PRIVATE_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfProtectedFields(getCodeMetricsResultWithValue(result.getNumberOfProtectedFields(), valueType, codeMetricsClassResult.getNumberOfProtectedFields(), "NO_OF_PROTECTED_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfDefaultFields(getCodeMetricsResultWithValue(result.getNumberOfDefaultFields(), valueType, codeMetricsClassResult.getNumberOfDefaultFields(), "NO_OF_DEFAULT_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfFinalFields(getCodeMetricsResultWithValue(result.getNumberOfFinalFields(), valueType, codeMetricsClassResult.getNumberOfFinalFields(), "NO_OF_FINAL_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setNumberOfSynchronizedFields(getCodeMetricsResultWithValue(result.getNumberOfSynchronizedFields(), valueType, codeMetricsClassResult.getNumberOfSynchronizedFields(), "NO_OF_SYNCHRONIZED_FIELDS", constructName, constructType, filename));
        codeMetricsClassResult.setModifiers(getCodeMetricsResultWithValue(result.getModifiers(), valueType, codeMetricsClassResult.getModifiers(), "NO_OF_MODIFIERS", constructName, constructType, filename));
    }

    private CodeMetricsDiffResult getCodeMetricsResultWithValue(int value, String type, CodeMetricsDiffResult existingResult, String metricName, String constructName, String constructType, String filename) {
        CodeMetricsDiffResult codeMetricsDiffResult = getCodeMetricsResult(existingResult);
        codeMetricsDiffResult.setConstructName(constructName);
        codeMetricsDiffResult.setFileName(filename);
        codeMetricsDiffResult.setConstructType(constructType);
        codeMetricsDiffResult.setMetricName(metricName);
        if(type == "new") {
            codeMetricsDiffResult.setNewValue(value);
        } else {
            codeMetricsDiffResult.setOldValue(value);
        }
        return codeMetricsDiffResult;
    }

    private CodeMetricsDiffResult getCodeMetricsResult(CodeMetricsDiffResult existingResult) {
        if(Objects.isNull(existingResult)) {
            return new CodeMetricsDiffResult();
        } else {
            return existingResult;
        }
    }
}
