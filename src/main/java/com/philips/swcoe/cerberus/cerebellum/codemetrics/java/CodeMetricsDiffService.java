package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsResult;
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
        String className = result.getClassName();
        codeMetricsMethodResult.setMethodName(className + "::" +methodResult.getMethodName());
        if(methodResult.isConstructor()) {
            codeMetricsMethodResult.setMethodName(className + ":CONSTRUCTOR:" +methodResult.getMethodName());
        }
        codeMetricsMethodResult.setType("METHOD");
        codeMetricsMethodResult.setFile(result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1));
        codeMetricsMethodResult.setComplexity(getCodeMetricsResultWithValue(methodResult.getWmc(), valueType, codeMetricsMethodResult.getComplexity(), "COMPLEXITY_OF_METHOD"));
        codeMetricsMethodResult.setParametersQty(getCodeMetricsResultWithValue(methodResult.getParametersQty(), valueType, codeMetricsMethodResult.getParametersQty(), "NO_OF_PARAMETERS"));
        codeMetricsMethodResult.setLinesOfCode(getCodeMetricsResultWithValue(methodResult.getLoc(), valueType, codeMetricsMethodResult.getLinesOfCode(), "LINES_OF_CODE"));
        codeMetricsMethodResult.setStartLineNo(getCodeMetricsResultWithValue(methodResult.getStartLine(), valueType, codeMetricsMethodResult.getStartLineNo(), "START_LINE_NO"));
        codeMetricsMethodResult.setComparisonsQty(getCodeMetricsResultWithValue(methodResult.getComparisonsQty(), valueType, codeMetricsMethodResult.getComparisonsQty(), "NO_OF_COMPARISONS"));
        codeMetricsMethodResult.setTryCatchQty(getCodeMetricsResultWithValue(methodResult.getTryCatchQty(), valueType, codeMetricsMethodResult.getTryCatchQty(), "NO_OF_TRYCATCH_BLOCKS"));
        codeMetricsMethodResult.setParenthesizedExpsQty(getCodeMetricsResultWithValue(methodResult.getParenthesizedExpsQty(), valueType, codeMetricsMethodResult.getParenthesizedExpsQty(), "NO_OF_PARENTHESIZED_EXPRESSIONS"));
        codeMetricsMethodResult.setStringLiteralsQty(getCodeMetricsResultWithValue(methodResult.getStringLiteralsQty(), valueType, codeMetricsMethodResult.getStringLiteralsQty(), "NO_OF_STRING_LITERALS"));
        codeMetricsMethodResult.setNumbersQty(getCodeMetricsResultWithValue(methodResult.getNumbersQty(), valueType, codeMetricsMethodResult.getNumbersQty(), "NO_OF_NOS"));
        codeMetricsMethodResult.setAssignmentsQty(getCodeMetricsResultWithValue(methodResult.getAssignmentsQty(), valueType, codeMetricsMethodResult.getAssignmentsQty(), "NO_OF_ASSIGNMENTS"));
        codeMetricsMethodResult.setMathOperationsQty(getCodeMetricsResultWithValue(methodResult.getMathOperationsQty(), valueType, codeMetricsMethodResult.getMathOperationsQty(), "NO_OF_MATH_OPERATIONS"));
        codeMetricsMethodResult.setVariablesQty(getCodeMetricsResultWithValue(methodResult.getVariablesQty(), valueType, codeMetricsMethodResult.getVariablesQty(), "NO_OF_VARIABLES"));
        codeMetricsMethodResult.setMaxNestedBlocks(getCodeMetricsResultWithValue(methodResult.getMaxNestedBlocks(), valueType, codeMetricsMethodResult.getMaxNestedBlocks(), "NO_OF_MAX_NESTED_BLOCKS"));
        codeMetricsMethodResult.setAnonymousClassesQty(getCodeMetricsResultWithValue(methodResult.getAnonymousClassesQty(), valueType, codeMetricsMethodResult.getAnonymousClassesQty(), "NO_OF_ANONYMOUS_CLASSES"));
        codeMetricsMethodResult.setSubClassesQty(getCodeMetricsResultWithValue(methodResult.getSubClassesQty(), valueType, codeMetricsMethodResult.getSubClassesQty(), "NO_OF_SUBCLASSES"));
        codeMetricsMethodResult.setLambdasQty(getCodeMetricsResultWithValue(methodResult.getLambdasQty(), valueType, codeMetricsMethodResult.getLambdasQty(), "NO_OF_LAMBDAS"));
        codeMetricsMethodResult.setLoopQty(getCodeMetricsResultWithValue(methodResult.getLoopQty(), valueType, codeMetricsMethodResult.getLoopQty(), "NO_OF_LOOPS"));
        codeMetricsMethodResult.setUniqueWordsQty(getCodeMetricsResultWithValue(methodResult.getUniqueWordsQty(), valueType, codeMetricsMethodResult.getUniqueWordsQty(), "NO_OF_UNIQUE_WORDS"));
    }

    private void transFormClassResult(CKClassResult result, CodeMetricsClassResult codeMetricsClassResult, String valueType) {
        codeMetricsClassResult.setFile(result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1));
        codeMetricsClassResult.setClassName(result.getClassName());
        codeMetricsClassResult.setType(result.getType().toUpperCase());
        codeMetricsClassResult.setDepthInheritanceTree(getCodeMetricsResultWithValue(result.getDit(), valueType, codeMetricsClassResult.getDepthInheritanceTree(), "DEPTH_INHERITANCE_TREE"));
        codeMetricsClassResult.setWeightMethodClass(getCodeMetricsResultWithValue(result.getWmc(), valueType, codeMetricsClassResult.getWeightMethodClass(), "WEIGHT_METHOD_CLASS"));
        codeMetricsClassResult.setCouplingBetweenObjects(getCodeMetricsResultWithValue(result.getCbo(), valueType, codeMetricsClassResult.getCouplingBetweenObjects(), "COUPLING_BETWEEN_OBJECTS"));
        codeMetricsClassResult.setResponseForClass(getCodeMetricsResultWithValue(result.getRfc(), valueType, codeMetricsClassResult.getResponseForClass(), "RESPONSE_FOR_A_CLASS"));
        codeMetricsClassResult.setNumberOfStaticInvocations(getCodeMetricsResultWithValue(result.getNosi(), valueType, codeMetricsClassResult.getNumberOfStaticInvocations(), "NO_OF_STATIC_INVOCATIONS"));
        codeMetricsClassResult.setLinesOfCode(getCodeMetricsResultWithValue(result.getLoc(), valueType, codeMetricsClassResult.getLinesOfCode(), "LINES_OF_CODE"));
        codeMetricsClassResult.setReturnQty(getCodeMetricsResultWithValue(result.getReturnQty(), valueType, codeMetricsClassResult.getReturnQty(), "NO_OF_RETURNS"));
        codeMetricsClassResult.setLoopQty(getCodeMetricsResultWithValue(result.getLoopQty(), valueType, codeMetricsClassResult.getLoopQty(), "NO_OF_LOOPS"));
        codeMetricsClassResult.setComparisonsQty(getCodeMetricsResultWithValue(result.getComparisonsQty(), valueType, codeMetricsClassResult.getComparisonsQty(), "NO_OF_COMPARISONS"));
        codeMetricsClassResult.setTryCatchQty(getCodeMetricsResultWithValue(result.getTryCatchQty(), valueType, codeMetricsClassResult.getTryCatchQty(), "NO_OF_TRYCATCH_BLOCKS"));
        codeMetricsClassResult.setParenthesizedExpsQty(getCodeMetricsResultWithValue(result.getParenthesizedExpsQty(), valueType, codeMetricsClassResult.getParenthesizedExpsQty(), "NO_OF_PARENTHESIZED_EXPRESSIONS"));
        codeMetricsClassResult.setStringLiteralsQty(getCodeMetricsResultWithValue(result.getStringLiteralsQty(), valueType, codeMetricsClassResult.getStringLiteralsQty(), "NO_OF_STRING_LITERALS"));
        codeMetricsClassResult.setNumbersQty(getCodeMetricsResultWithValue(result.getNumbersQty(), valueType, codeMetricsClassResult.getNumbersQty(), "NO_OF_NOS"));
        codeMetricsClassResult.setAssignmentsQty(getCodeMetricsResultWithValue(result.getAssignmentsQty(), valueType, codeMetricsClassResult.getAssignmentsQty(), "NO_OF_ASSIGNMENTS"));
        codeMetricsClassResult.setMathOperationsQty(getCodeMetricsResultWithValue(result.getMathOperationsQty(), valueType, codeMetricsClassResult.getMathOperationsQty(), "NO_OF_MATH_OPERATIONS"));
        codeMetricsClassResult.setVariablesQty(getCodeMetricsResultWithValue(result.getVariablesQty(), valueType, codeMetricsClassResult.getVariablesQty(), "NO_OF_VARIABLES"));
        codeMetricsClassResult.setMaxNestedBlocks(getCodeMetricsResultWithValue(result.getMaxNestedBlocks(), valueType, codeMetricsClassResult.getMaxNestedBlocks(), "NO_OF_MAX_NESTED_BLOCKS"));
        codeMetricsClassResult.setAnonymousClassesQty(getCodeMetricsResultWithValue(result.getAnonymousClassesQty(), valueType, codeMetricsClassResult.getAnonymousClassesQty(), "NO_OF_ANONYMOUS_CLASSES"));
        codeMetricsClassResult.setSubClassesQty(getCodeMetricsResultWithValue(result.getSubClassesQty(), valueType, codeMetricsClassResult.getSubClassesQty(), "NO_OF_SUBCLASSES"));
        codeMetricsClassResult.setLambdasQty(getCodeMetricsResultWithValue(result.getLambdasQty(), valueType, codeMetricsClassResult.getLambdasQty(), "NO_OF_LAMBDAS"));
        codeMetricsClassResult.setUniqueWordsQty(getCodeMetricsResultWithValue(result.getUniqueWordsQty(), valueType, codeMetricsClassResult.getUniqueWordsQty(), "NO_OF_UNIQUE_WORDS"));
        codeMetricsClassResult.setNumberOfMethods(getCodeMetricsResultWithValue(result.getNumberOfMethods(), valueType, codeMetricsClassResult.getNumberOfMethods(), "NO_OF_METHODS"));
        codeMetricsClassResult.setNumberOfStaticMethods(getCodeMetricsResultWithValue(result.getNumberOfStaticMethods(), valueType, codeMetricsClassResult.getNumberOfStaticMethods(), "NO_OF_STATIC_METHODS"));
        codeMetricsClassResult.setNumberOfPublicMethods(getCodeMetricsResultWithValue(result.getNumberOfPublicMethods(), valueType, codeMetricsClassResult.getNumberOfPublicMethods(), "NO_OF_PUBLIC_METHODS"));
        codeMetricsClassResult.setNumberOfPrivateMethods(getCodeMetricsResultWithValue(result.getNumberOfPrivateMethods(), valueType, codeMetricsClassResult.getNumberOfPrivateMethods(), "NO_OF_PRIVATE_METHODS"));
        codeMetricsClassResult.setNumberOfProtectedMethods(getCodeMetricsResultWithValue(result.getNumberOfProtectedMethods(), valueType, codeMetricsClassResult.getNumberOfProtectedMethods(), "NO_OF_PROTECTED_METHODS"));
        codeMetricsClassResult.setNumberOfDefaultMethods(getCodeMetricsResultWithValue(result.getNumberOfDefaultMethods(), valueType, codeMetricsClassResult.getNumberOfDefaultMethods(), "NO_OF_DEFAULT_METHODS"));
        codeMetricsClassResult.setNumberOfAbstractMethods(getCodeMetricsResultWithValue(result.getNumberOfAbstractMethods(), valueType, codeMetricsClassResult.getNumberOfAbstractMethods(), "NO_OF_ABSTRACT_METHODS"));
        codeMetricsClassResult.setNumberOfFinalMethods(getCodeMetricsResultWithValue(result.getNumberOfFinalMethods(), valueType, codeMetricsClassResult.getNumberOfFinalFields(), "NO_OF_FINAL_METHODS"));
        codeMetricsClassResult.setNumberOfSynchronizedMethods(getCodeMetricsResultWithValue(result.getNumberOfSynchronizedMethods(), valueType, codeMetricsClassResult.getNumberOfSynchronizedFields(), "NO_OF_SYNCHRONIZED_METHODS"));
        codeMetricsClassResult.setNumberOfFields(getCodeMetricsResultWithValue(result.getNumberOfFields(), valueType, codeMetricsClassResult.getNumberOfFields(), "NO_OF_FIELDS"));
        codeMetricsClassResult.setNumberOfStaticFields(getCodeMetricsResultWithValue(result.getNumberOfStaticFields(), valueType, codeMetricsClassResult.getNumberOfStaticFields(), "NO_OF_STATIC_FIELDS"));
        codeMetricsClassResult.setNumberOfPublicFields(getCodeMetricsResultWithValue(result.getNumberOfPublicFields(), valueType, codeMetricsClassResult.getNumberOfPublicFields(), "NO_OF_PUBLIC_FIELDS"));
        codeMetricsClassResult.setNumberOfPrivateFields(getCodeMetricsResultWithValue(result.getNumberOfPrivateFields(), valueType, codeMetricsClassResult.getNumberOfPrivateFields(), "NO_OF_PRIVATE_FIELDS"));
        codeMetricsClassResult.setNumberOfProtectedFields(getCodeMetricsResultWithValue(result.getNumberOfProtectedFields(), valueType, codeMetricsClassResult.getNumberOfProtectedFields(), "NO_OF_PROTECTED_FIELDS"));
        codeMetricsClassResult.setNumberOfDefaultFields(getCodeMetricsResultWithValue(result.getNumberOfDefaultFields(), valueType, codeMetricsClassResult.getNumberOfDefaultFields(), "NO_OF_DEFAULT_FIELDS"));
        codeMetricsClassResult.setNumberOfFinalFields(getCodeMetricsResultWithValue(result.getNumberOfFinalFields(), valueType, codeMetricsClassResult.getNumberOfFinalFields(), "NO_OF_FINAL_FIELDS"));
        codeMetricsClassResult.setNumberOfSynchronizedFields(getCodeMetricsResultWithValue(result.getNumberOfSynchronizedFields(), valueType, codeMetricsClassResult.getNumberOfSynchronizedFields(), "NO_OF_SYNCHRONIZED_FIELDS"));
        codeMetricsClassResult.setModifiers(getCodeMetricsResultWithValue(result.getModifiers(), valueType, codeMetricsClassResult.getModifiers(), "NO_OF_MODIFIERS"));
    }

    private CodeMetricsResult getCodeMetricsResultWithValue(int value, String type, CodeMetricsResult existingResult, String metricName) {
        CodeMetricsResult codeMetricsResult = getCodeMetricsResult(existingResult);
        codeMetricsResult.setMetricName(metricName);
        if(type == "new") {
            codeMetricsResult.setNewValue(value);
        } else {
            codeMetricsResult.setOldValue(value);
        }
        return codeMetricsResult;
    }

    private CodeMetricsResult getCodeMetricsResult(CodeMetricsResult existingResult) {
        if(Objects.isNull(existingResult)) {
            return new CodeMetricsResult();
        } else {
            return existingResult;
        }
    }
}
