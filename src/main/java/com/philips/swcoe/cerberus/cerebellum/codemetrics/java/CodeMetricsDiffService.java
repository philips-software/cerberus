package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import static com.philips.swcoe.cerberus.constants.ProgramConstants.PATH_SEPARATOR;
import com.github.mauricioaniche.ck.CK;
import com.github.mauricioaniche.ck.CKClassResult;
import com.github.mauricioaniche.ck.CKMethodResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsDiffResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsMethodResult;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

public class CodeMetricsDiffService {

    private String previousPath;
    private String currentPath;

    public CodeMetricsDiffService(String previousPath, String currentPath) {
        this.previousPath = previousPath;
        this.currentPath = currentPath;
    }

    public List<CodeMetricsClassResult> getMetricsFromSourceCode() {
        List<CodeMetricsClassResult> codeMetricsClassResultsList =
            new ArrayList<CodeMetricsClassResult>();
        new CK().calculate(currentPath, false, result -> {
            CodeMetricsClassResult codeMetricsClassResult = new CodeMetricsClassResult();
            transformResults(result, codeMetricsClassResult, "new");
            codeMetricsClassResultsList.add(codeMetricsClassResult);
        });

        new CK().calculate(previousPath, false, result -> {
            Optional<CodeMetricsClassResult> existingNewResults = codeMetricsClassResultsList
                .stream()
                .filter(metrics -> metrics.getFile().contains(
                    (result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1))))
                .findFirst();
            if (existingNewResults.isPresent()) {
                transformResults(result, existingNewResults.get(), "old");
            } else {
                CodeMetricsClassResult codeMetricsClassResult = new CodeMetricsClassResult();
                transformResults(result, codeMetricsClassResult, "old");
                codeMetricsClassResultsList.add(codeMetricsClassResult);
            }
        });

        return codeMetricsClassResultsList;
    }

    private void transformResults(CKClassResult result,
                                  CodeMetricsClassResult codeMetricsClassResult, String valueType) {
        transFormClassResult(result, codeMetricsClassResult, valueType);
        transFormMethodResult(result, codeMetricsClassResult, valueType);
    }

    private void transFormMethodResult(CKClassResult result,
                                       CodeMetricsClassResult codeMetricsClassResult,
                                       String valueType) {
        Set<CKMethodResult> setOfMethodResults = result.getMethods();
        List<CodeMetricsMethodResult> listOfMethodMetricsOfClass =
            new ArrayList<CodeMetricsMethodResult>();
        setOfMethodResults.stream().forEach(methodResult -> {
            CodeMetricsMethodResult codeMetricsMethodResult =
                getMethodMetricsResultForTransform(codeMetricsClassResult, methodResult);
            setupMethodMetrics(result, valueType, methodResult, codeMetricsMethodResult);
            listOfMethodMetricsOfClass.add(codeMetricsMethodResult);
        });
        codeMetricsClassResult.setMethodMetrics(listOfMethodMetricsOfClass);
    }

    private CodeMetricsMethodResult getMethodMetricsResultForTransform(
        CodeMetricsClassResult codeMetricsClassResult, CKMethodResult ckMethodResult) {
        Optional<CodeMetricsMethodResult> existingMethodMetrics =
            Optional.ofNullable(codeMetricsClassResult.getMethodMetrics()).map(Collection::stream)
                .orElseGet(Stream::empty)
                .filter(methodMetrics -> methodMetrics.getMethodName()
                    .contains(ckMethodResult.getMethodName())).findFirst();
        if (existingMethodMetrics.isPresent()) {
            return existingMethodMetrics.get();
        } else {
            return new CodeMetricsMethodResult();
        }
    }

    private void setupMethodMetrics(CKClassResult result, String valueType,
                                    CKMethodResult methodResult,
                                    CodeMetricsMethodResult codeMetricsMethodResult) {
        String filename =
            result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1);
        String constructType = "METHOD";
        String className = result.getClassName();
        String constructName = className + "::" + methodResult.getMethodName();
        if (methodResult.isConstructor()) {
            constructName = className + ":CONSTRUCTOR:" + methodResult.getMethodName();
        }
        codeMetricsMethodResult.setMethodName(constructName);
        codeMetricsMethodResult.setType(constructType);
        codeMetricsMethodResult.setFile(filename);
        codeMetricsMethodResult.setComplexity(
            getCodeMetricsResultWithValue(methodResult.getWmc(), valueType,
                codeMetricsMethodResult.getComplexity(), "COMPLEXITY_OF_METHOD", constructName,
                constructType, filename));
        codeMetricsMethodResult.setParametersCount(
            getCodeMetricsResultWithValue(methodResult.getParametersQty(), valueType,
                codeMetricsMethodResult.getParametersCount(), "NO_OF_PARAMETERS", constructName,
                constructType, filename));
        codeMetricsMethodResult.setLinesOfCode(
            getCodeMetricsResultWithValue(methodResult.getLoc(), valueType,
                codeMetricsMethodResult.getLinesOfCode(), "LINES_OF_CODE", constructName,
                constructType, filename));
        codeMetricsMethodResult.setStartLineNo(
            getCodeMetricsResultWithValue(methodResult.getStartLine(), valueType,
                codeMetricsMethodResult.getStartLineNo(), "START_LINE_NO", constructName,
                constructType, filename));
        codeMetricsMethodResult.setComparisonsCount(
            getCodeMetricsResultWithValue(methodResult.getComparisonsQty(), valueType,
                codeMetricsMethodResult.getComparisonsCount(), "NO_OF_COMPARISONS", constructName,
                constructType, filename));
        codeMetricsMethodResult.setTryCatchCount(
            getCodeMetricsResultWithValue(methodResult.getTryCatchQty(), valueType,
                codeMetricsMethodResult.getTryCatchCount(), "NO_OF_TRYCATCH_BLOCKS", constructName,
                constructType, filename));
        codeMetricsMethodResult.setParenthesizedExpsCount(
            getCodeMetricsResultWithValue(methodResult.getParenthesizedExpsQty(), valueType,
                codeMetricsMethodResult.getParenthesizedExpsCount(),
                "NO_OF_PARENTHESIZED_EXPRESSIONS", constructName, constructType, filename));
        codeMetricsMethodResult.setStringLiteralsCount(
            getCodeMetricsResultWithValue(methodResult.getStringLiteralsQty(), valueType,
                codeMetricsMethodResult.getStringLiteralsCount(), "NO_OF_STRING_LITERALS",
                constructName, constructType, filename));
        codeMetricsMethodResult.setNumbersCount(
            getCodeMetricsResultWithValue(methodResult.getNumbersQty(), valueType,
                codeMetricsMethodResult.getNumbersCount(), "NO_OF_NOS", constructName,
                constructType, filename));
        codeMetricsMethodResult.setAssignmentsCount(
            getCodeMetricsResultWithValue(methodResult.getAssignmentsQty(), valueType,
                codeMetricsMethodResult.getAssignmentsCount(), "NO_OF_ASSIGNMENTS", constructName,
                constructType, filename));
        codeMetricsMethodResult.setMathOperationsCount(
            getCodeMetricsResultWithValue(methodResult.getMathOperationsQty(), valueType,
                codeMetricsMethodResult.getMathOperationsCount(), "NO_OF_MATH_OPERATIONS",
                constructName, constructType, filename));
        codeMetricsMethodResult.setVariablesCount(
            getCodeMetricsResultWithValue(methodResult.getVariablesQty(), valueType,
                codeMetricsMethodResult.getVariablesCount(), "NO_OF_VARIABLES", constructName,
                constructType, filename));
        codeMetricsMethodResult.setMaxNestedBlocks(
            getCodeMetricsResultWithValue(methodResult.getMaxNestedBlocks(), valueType,
                codeMetricsMethodResult.getMaxNestedBlocks(), "NO_OF_MAX_NESTED_BLOCKS",
                constructName, constructType, filename));
        codeMetricsMethodResult.setAnonymousClassesCount(
            getCodeMetricsResultWithValue(methodResult.getAnonymousClassesQty(), valueType,
                codeMetricsMethodResult.getAnonymousClassesCount(), "NO_OF_ANONYMOUS_CLASSES",
                constructName, constructType, filename));
        codeMetricsMethodResult.setSubClassesCount(
            getCodeMetricsResultWithValue(methodResult.getSubClassesQty(), valueType,
                codeMetricsMethodResult.getSubClassesCount(), "NO_OF_SUBCLASSES", constructName,
                constructType, filename));
        codeMetricsMethodResult.setLambdasCount(
            getCodeMetricsResultWithValue(methodResult.getLambdasQty(), valueType,
                codeMetricsMethodResult.getLambdasCount(), "NO_OF_LAMBDAS", constructName,
                constructType, filename));
        codeMetricsMethodResult.setLoopCount(
            getCodeMetricsResultWithValue(methodResult.getLoopQty(), valueType,
                codeMetricsMethodResult.getLoopCount(), "NO_OF_LOOPS", constructName, constructType,
                filename));
        codeMetricsMethodResult.setUniqueWordsCount(
            getCodeMetricsResultWithValue(methodResult.getUniqueWordsQty(), valueType,
                codeMetricsMethodResult.getUniqueWordsCount(), "NO_OF_UNIQUE_WORDS", constructName,
                constructType, filename));
    }

    private void transFormClassResult(CKClassResult result,
                                      CodeMetricsClassResult codeMetricsClassResult,
                                      String valueType) {
        String filename =
            result.getFile().substring(result.getFile().lastIndexOf(PATH_SEPARATOR) + 1);
        String constructName = result.getClassName();
        String constructType = result.getType().toUpperCase();
        codeMetricsClassResult.setFile(filename);
        codeMetricsClassResult.setClassName(constructName);
        codeMetricsClassResult.setType(constructType);
        codeMetricsClassResult.setDepthInheritanceTree(
            getCodeMetricsResultWithValue(result.getDit(), valueType,
                codeMetricsClassResult.getDepthInheritanceTree(), "DEPTH_INHERITANCE_TREE",
                constructName, constructType, filename));
        codeMetricsClassResult.setWeightMethodClass(
            getCodeMetricsResultWithValue(result.getWmc(), valueType,
                codeMetricsClassResult.getWeightMethodClass(), "WEIGHT_METHOD_CLASS", constructName,
                constructType, filename));
        codeMetricsClassResult.setCouplingBetweenObjects(
            getCodeMetricsResultWithValue(result.getCbo(), valueType,
                codeMetricsClassResult.getCouplingBetweenObjects(), "COUPLING_BETWEEN_OBJECTS",
                constructName, constructType, filename));
        codeMetricsClassResult.setResponseForClass(
            getCodeMetricsResultWithValue(result.getRfc(), valueType,
                codeMetricsClassResult.getResponseForClass(), "RESPONSE_FOR_A_CLASS", constructName,
                constructType, filename));
        codeMetricsClassResult.setNumberOfStaticInvocations(
            getCodeMetricsResultWithValue(result.getNosi(), valueType,
                codeMetricsClassResult.getNumberOfStaticInvocations(), "NO_OF_STATIC_INVOCATIONS",
                constructName, constructType, filename));
        codeMetricsClassResult.setLinesOfCode(
            getCodeMetricsResultWithValue(result.getLoc(), valueType,
                codeMetricsClassResult.getLinesOfCode(), "LINES_OF_CODE", constructName,
                constructType, filename));
        codeMetricsClassResult.setReturnCount(
            getCodeMetricsResultWithValue(result.getReturnQty(), valueType,
                codeMetricsClassResult.getReturnCount(), "NO_OF_RETURNS", constructName,
                constructType, filename));
        codeMetricsClassResult.setLoopCount(
            getCodeMetricsResultWithValue(result.getLoopQty(), valueType,
                codeMetricsClassResult.getLoopCount(), "NO_OF_LOOPS", constructName, constructType,
                filename));
        codeMetricsClassResult.setComparisonsCount(
            getCodeMetricsResultWithValue(result.getComparisonsQty(), valueType,
                codeMetricsClassResult.getComparisonsCount(), "NO_OF_COMPARISONS", constructName,
                constructType, filename));
        codeMetricsClassResult.setTryCatchCount(
            getCodeMetricsResultWithValue(result.getTryCatchQty(), valueType,
                codeMetricsClassResult.getTryCatchCount(), "NO_OF_TRYCATCH_BLOCKS", constructName,
                constructType, filename));
        codeMetricsClassResult.setParenthesizedExpsCount(
            getCodeMetricsResultWithValue(result.getParenthesizedExpsQty(), valueType,
                codeMetricsClassResult.getParenthesizedExpsCount(),
                "NO_OF_PARENTHESIZED_EXPRESSIONS", constructName, constructType, filename));
        codeMetricsClassResult.setStringLiteralsCount(
            getCodeMetricsResultWithValue(result.getStringLiteralsQty(), valueType,
                codeMetricsClassResult.getStringLiteralsCount(), "NO_OF_STRING_LITERALS",
                constructName, constructType, filename));
        codeMetricsClassResult.setNumbersCount(
            getCodeMetricsResultWithValue(result.getNumbersQty(), valueType,
                codeMetricsClassResult.getNumbersCount(), "NO_OF_NOS", constructName, constructType,
                filename));
        codeMetricsClassResult.setAssignmentsCount(
            getCodeMetricsResultWithValue(result.getAssignmentsQty(), valueType,
                codeMetricsClassResult.getAssignmentsCount(), "NO_OF_ASSIGNMENTS", constructName,
                constructType, filename));
        codeMetricsClassResult.setMathOperationsCount(
            getCodeMetricsResultWithValue(result.getMathOperationsQty(), valueType,
                codeMetricsClassResult.getMathOperationsCount(), "NO_OF_MATH_OPERATIONS",
                constructName, constructType, filename));
        codeMetricsClassResult.setVariablesCount(
            getCodeMetricsResultWithValue(result.getVariablesQty(), valueType,
                codeMetricsClassResult.getVariablesCount(), "NO_OF_VARIABLES", constructName,
                constructType, filename));
        codeMetricsClassResult.setMaxNestedBlocks(
            getCodeMetricsResultWithValue(result.getMaxNestedBlocks(), valueType,
                codeMetricsClassResult.getMaxNestedBlocks(), "NO_OF_MAX_NESTED_BLOCKS",
                constructName, constructType, filename));
        codeMetricsClassResult.setAnonymousClassesCount(
            getCodeMetricsResultWithValue(result.getAnonymousClassesQty(), valueType,
                codeMetricsClassResult.getAnonymousClassesCount(), "NO_OF_ANONYMOUS_CLASSES",
                constructName, constructType, filename));
        codeMetricsClassResult.setSubClassesCount(
            getCodeMetricsResultWithValue(result.getSubClassesQty(), valueType,
                codeMetricsClassResult.getSubClassesCount(), "NO_OF_SUBCLASSES", constructName,
                constructType, filename));
        codeMetricsClassResult.setLambdasCount(
            getCodeMetricsResultWithValue(result.getLambdasQty(), valueType,
                codeMetricsClassResult.getLambdasCount(), "NO_OF_LAMBDAS", constructName,
                constructType, filename));
        codeMetricsClassResult.setUniqueWordsCount(
            getCodeMetricsResultWithValue(result.getUniqueWordsQty(), valueType,
                codeMetricsClassResult.getUniqueWordsCount(), "NO_OF_UNIQUE_WORDS", constructName,
                constructType, filename));
        codeMetricsClassResult.setMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfMethods(), valueType,
                codeMetricsClassResult.getMethodsCount(), "NO_OF_METHODS", constructName,
                constructType, filename));
        codeMetricsClassResult.setStaticMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfStaticMethods(), valueType,
                codeMetricsClassResult.getStaticMethodsCount(), "NO_OF_STATIC_METHODS",
                constructName, constructType, filename));
        codeMetricsClassResult.setPublicMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfPublicMethods(), valueType,
                codeMetricsClassResult.getPublicMethodsCount(), "NO_OF_PUBLIC_METHODS",
                constructName, constructType, filename));
        codeMetricsClassResult.setPrivateMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfPrivateMethods(), valueType,
                codeMetricsClassResult.getPrivateMethodsCount(), "NO_OF_PRIVATE_METHODS",
                constructName, constructType, filename));
        codeMetricsClassResult.setProtectedMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfProtectedMethods(), valueType,
                codeMetricsClassResult.getProtectedMethodsCount(), "NO_OF_PROTECTED_METHODS",
                constructName, constructType, filename));
        codeMetricsClassResult.setDefaultMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfDefaultMethods(), valueType,
                codeMetricsClassResult.getDefaultMethodsCount(), "NO_OF_DEFAULT_METHODS",
                constructName, constructType, filename));
        codeMetricsClassResult.setAbstractMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfAbstractMethods(), valueType,
                codeMetricsClassResult.getAbstractMethodsCount(), "NO_OF_ABSTRACT_METHODS",
                constructName, constructType, filename));
        codeMetricsClassResult.setFinalMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfFinalMethods(), valueType,
                codeMetricsClassResult.getFinalFieldsCount(), "NO_OF_FINAL_METHODS", constructName,
                constructType, filename));
        codeMetricsClassResult.setSynchronizedMethodsCount(
            getCodeMetricsResultWithValue(result.getNumberOfSynchronizedMethods(), valueType,
                codeMetricsClassResult.getSynchronizedFieldsCount(), "NO_OF_SYNCHRONIZED_METHODS",
                constructName, constructType, filename));
        codeMetricsClassResult.setFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfFields(), valueType,
                codeMetricsClassResult.getFieldsCount(), "NO_OF_FIELDS", constructName,
                constructType, filename));
        codeMetricsClassResult.setStaticFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfStaticFields(), valueType,
                codeMetricsClassResult.getStaticFieldsCount(), "NO_OF_STATIC_FIELDS", constructName,
                constructType, filename));
        codeMetricsClassResult.setPublicFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfPublicFields(), valueType,
                codeMetricsClassResult.getPublicFieldsCount(), "NO_OF_PUBLIC_FIELDS", constructName,
                constructType, filename));
        codeMetricsClassResult.setPrivateFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfPrivateFields(), valueType,
                codeMetricsClassResult.getPrivateFieldsCount(), "NO_OF_PRIVATE_FIELDS",
                constructName, constructType, filename));
        codeMetricsClassResult.setProtectedFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfProtectedFields(), valueType,
                codeMetricsClassResult.getProtectedFieldsCount(), "NO_OF_PROTECTED_FIELDS",
                constructName, constructType, filename));
        codeMetricsClassResult.setDefaultFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfDefaultFields(), valueType,
                codeMetricsClassResult.getDefaultFieldsCount(), "NO_OF_DEFAULT_FIELDS",
                constructName, constructType, filename));
        codeMetricsClassResult.setFinalFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfFinalFields(), valueType,
                codeMetricsClassResult.getFinalFieldsCount(), "NO_OF_FINAL_FIELDS", constructName,
                constructType, filename));
        codeMetricsClassResult.setSynchronizedFieldsCount(
            getCodeMetricsResultWithValue(result.getNumberOfSynchronizedFields(), valueType,
                codeMetricsClassResult.getSynchronizedFieldsCount(), "NO_OF_SYNCHRONIZED_FIELDS",
                constructName, constructType, filename));
        codeMetricsClassResult.setModifiersCount(
            getCodeMetricsResultWithValue(result.getModifiers(), valueType,
                codeMetricsClassResult.getModifiersCount(), "NO_OF_MODIFIERS", constructName,
                constructType, filename));
    }

    private CodeMetricsDiffResult getCodeMetricsResultWithValue(int value, String type,
                                                                CodeMetricsDiffResult existingResult,
                                                                String metricName,
                                                                String constructName,
                                                                String constructType,
                                                                String filename) {
        CodeMetricsDiffResult codeMetricsDiffResult = getCodeMetricsResult(existingResult);
        codeMetricsDiffResult.setConstructName(constructName);
        codeMetricsDiffResult.setFileName(filename);
        codeMetricsDiffResult.setConstructType(constructType);
        codeMetricsDiffResult.setMetricName(metricName);
        if ("new".equals(type)) {
            codeMetricsDiffResult.setNewValue(value);
        } else {
            codeMetricsDiffResult.setOldValue(value);
        }
        return codeMetricsDiffResult;
    }

    private CodeMetricsDiffResult getCodeMetricsResult(CodeMetricsDiffResult existingResult) {
        if (Objects.isNull(existingResult)) {
            return new CodeMetricsDiffResult();
        } else {
            return existingResult;
        }
    }
}
