package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.PATH_SEPARATOR;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.RESOURCES;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE_CURRENT;
import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.TEST_JAVA_CODE_PREVIOUS;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsDiffResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsMethodResult;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

public class CodeMetricsDiffServiceTest {

    private final String previousPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_PREVIOUS;
    private final String currentPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_CURRENT;
    private final String configPath = RESOURCES + PATH_SEPARATOR + "class_metrics_to_display.properties";
    private CodeMetricsDiffService codeMetricsDiffService;
    private List<CodeMetricsClassResult> codeMetricsClassResultList;
    private final Function<String, CodeMetricsClassResult> javaCodeMetricsResultToTest =
        (filename) -> codeMetricsClassResultList.stream()
            .filter(f -> f.getFile().equalsIgnoreCase(filename)).findFirst().get();

    @BeforeEach
    public void setupJavaCodeMetricsWithDiff() {
        codeMetricsDiffService = new CodeMetricsDiffService(previousPath, currentPath);
        codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
    }

    @Test
    public void shouldGetMetrics() throws Exception {
        assertEquals(6, codeMetricsClassResultList.size());
    }

    @Test
    public void shouldGetMetricsForWhichWasRemovedInCurrent() throws Exception {
        CodeMetricsClassResult result = codeMetricsClassResultList.stream().filter(
            javaCodeMetricsClassResult -> javaCodeMetricsClassResult.getFile()
                .contains("Rhombus.java"))
            .findFirst().get();
        assertEquals("Rhombus.java", result.getFile());
        assertEquals("Shapes.Rhombus", result.getClassName());
        assertDoesNotThrowForEachClassMetric(result);
    }

    @Test
    public void shouldSetResultForAllMetricsforNewlyIntroducedFile() {
        CodeMetricsClassResult resultToEvaluate = codeMetricsClassResultList.stream().filter(
            javaCodeMetricsClassResult -> javaCodeMetricsClassResult.getFile()
                .contains("Triangle.java"))
            .findFirst().get();
        assertDoesNotThrowForEachClassMetric(resultToEvaluate);
    }


    @Test
    public void shouldGetMetricsWhichWasNewlyCreatedInCurrent() throws Exception {
        Optional<CodeMetricsClassResult> result = codeMetricsClassResultList.stream().filter(
            javaCodeMetricsClassResult -> javaCodeMetricsClassResult.getFile()
                .contains("Triangle.java"))
            .findFirst();
        assertEquals("Triangle.java", result.get().getFile());
        assertEquals("Shapes.Triangle", result.get().getClassName());
        assertEquals(17, result.get().getLinesOfCode().getNewValue());
        assertEquals(4, result.get().getMethodsCount().getNewValue());
        assertEquals(4, result.get().getPublicMethodsCount().getNewValue());
        assertEquals(2, result.get().getReturnCount().getNewValue());
        assertEquals(0, result.get().getProtectedMethodsCount().getNewValue());
        assertEquals(2, result.get().getDepthInheritanceTree().getNewValue());
        assertEquals(4, result.get().getWeightMethodClass().getNewValue());
    }


    @Test
    public void metricsShouldHaveAtleastSetFilyTypeAndClass() throws Exception {
        assertNotNull(javaCodeMetricsResultToTest.apply("Rectangle.java").getFile());
        assertNotNull(javaCodeMetricsResultToTest.apply("Rectangle.java").getType());
        assertNotNull(javaCodeMetricsResultToTest.apply("Rectangle.java").getClassName());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForLinesOfCode() throws Exception {
        assertEquals(27,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getLinesOfCode().getOldValue());
        assertEquals(37,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getLinesOfCode().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfMethods() throws Exception {
        assertEquals(7,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getMethodsCount().getOldValue());
        assertEquals(10,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getMethodsCount().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfPrivateProperties()
        throws Exception {
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getPrivateFieldsCount()
            .getOldValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfPublicMethods() throws Exception {
        assertEquals(3, javaCodeMetricsResultToTest.apply("Rectangle.java").getPublicMethodsCount()
            .getOldValue());
        assertEquals(5, javaCodeMetricsResultToTest.apply("Rectangle.java").getPublicMethodsCount()
            .getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfFields() throws Exception {
        assertEquals(2,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getFieldsCount().getOldValue());
        assertEquals(3,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getFieldsCount().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfProtectedMethods() throws Exception {
        assertEquals(2,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getProtectedMethodsCount()
                .getOldValue());
        assertEquals(3,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getProtectedMethodsCount()
                .getNewValue());
    }


    @Test
    public void metricsShouldHaveBothOldAndNewValuesForDepthInheritanceTree() throws Exception {
        assertEquals(2,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getDepthInheritanceTree()
                .getOldValue());
        assertEquals(2,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getDepthInheritanceTree()
                .getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForWMC() throws Exception {
        assertEquals(7, javaCodeMetricsResultToTest.apply("Rectangle.java").getWeightMethodClass()
            .getOldValue());
        assertEquals(10, javaCodeMetricsResultToTest.apply("Rectangle.java").getWeightMethodClass()
            .getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForCBO() throws Exception {
        assertEquals(2,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getCouplingBetweenObjects()
                .getOldValue());
        assertEquals(3,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getCouplingBetweenObjects()
                .getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForRFC() throws Exception {
        assertEquals(4, javaCodeMetricsResultToTest.apply("Rectangle.java").getResponseForClass()
            .getOldValue());
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getResponseForClass()
            .getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNOSI() throws Exception {
        assertEquals(0,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfStaticInvocations()
                .getOldValue());
        assertEquals(0,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfStaticInvocations()
                .getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForRtnQuantity() throws Exception {
        assertEquals(4,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getReturnCount().getOldValue());
        assertEquals(5,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getReturnCount().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForComparisionQuantity() throws Exception {
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getComparisonsCount()
            .getOldValue());
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getComparisonsCount()
            .getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForTryCatchQty() throws Exception {
        assertEquals(0,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getTryCatchCount().getOldValue());
        assertEquals(0,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getTryCatchCount().getNewValue());
    }

    @Test
    public void metricsShouldHaveMethodLevelMetricsRecordedForEachFile() throws Exception {
        assertEquals(5, javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().size());
        assertEquals(7,
            javaCodeMetricsResultToTest.apply("Rectangle.java").getMethodMetrics().size());
        assertEquals(1, javaCodeMetricsResultToTest.apply("Main.java").getMethodMetrics().size());
        assertEquals(6, javaCodeMetricsResultToTest.apply("Shape.java").getMethodMetrics().size());
        assertEquals(7,
            javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().size());
    }

    @Test
    public void metricsShouldHaveMethodLevelMetricsSetToNotNull() throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult =
            javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(1);
        assertDoesNotThrowForEachMethodMetric(codeMetricsMethodResult);
    }


    @Test
    public void methodLevelMetricsRecordedShouldHaveOldAndNewCyclometicComplexity()
        throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult =
            javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(0);
        assertEquals(1, codeMetricsMethodResult.getComplexity().getNewValue());
        assertEquals(1, codeMetricsMethodResult.getComplexity().getOldValue());
    }

    @Test
    public void methodLevelMetricsRecordedShouldHaveOldAndNewCyclometicComplexityForNonExistantInCurrent()
        throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode();
        CodeMetricsMethodResult codeMetricsMethodResult =
            javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().get(0);
        assertEquals(0, codeMetricsMethodResult.getComplexity().getNewValue());
        assertEquals(1, codeMetricsMethodResult.getComplexity().getOldValue());
    }

    @Test
    public void methodLevelMetricsRecordedShouldHaveOldAndNewLinesOfCode() throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult =
            javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(0);
        assertEquals(3, codeMetricsMethodResult.getLinesOfCode().getNewValue());
        assertEquals(3, codeMetricsMethodResult.getLinesOfCode().getOldValue());
    }

    @Test
    public void methodLevelMetricsRecordedShouldHaveOldAndNewLinesOfCodeForNonExistantInCurrent()
        throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode();
        CodeMetricsMethodResult codeMetricsMethodResult =
            javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().get(0);
        assertEquals(0, codeMetricsMethodResult.getLinesOfCode().getNewValue());
        assertEquals(3, codeMetricsMethodResult.getLinesOfCode().getOldValue());
    }

    @Test
    public void methodLevelMetricsRecordedShouldHaveOldAndNewStartLineNo() throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult =
            javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(0);
        assertEquals(6, codeMetricsMethodResult.getStartLineNo().getNewValue());
        assertEquals(6, codeMetricsMethodResult.getStartLineNo().getOldValue());
    }

    @Test
    public void methodLevelMetricsRecordedShouldHaveOldAndNewStartLineNoForNonExistantInCurrent()
        throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList =
            codeMetricsDiffService.getMetricsFromSourceCode();
        CodeMetricsMethodResult codeMetricsMethodResult =
            javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().get(0);
        assertEquals(0, codeMetricsMethodResult.getStartLineNo().getNewValue());
        assertEquals(16, codeMetricsMethodResult.getStartLineNo().getOldValue());
    }

    private void assertDoesNotThrowForEachClassMetric(CodeMetricsClassResult result) {
        Stream<Method> methodStream = Arrays.stream(CodeMetricsClassResult.class.getMethods())
            .filter(method -> method.getReturnType().equals(CodeMetricsDiffResult.class));
        methodStream.forEach(method -> {
            assertDoesNotThrow(() -> {
                CodeMetricsDiffResult codeMetricsDiffResult =
                    (CodeMetricsDiffResult) method.invoke(result);
                assertPropertiesOfCodeMetricsDiffResult(codeMetricsDiffResult);
            }, method.getName() + "Thrown Exception while invoking");
        });
    }

    private void assertDoesNotThrowForEachMethodMetric(CodeMetricsMethodResult result) {
        Stream<Method> methodStream = Arrays.stream(CodeMetricsMethodResult.class.getMethods())
            .filter(method -> method.getReturnType().equals(CodeMetricsDiffResult.class));
        methodStream.forEach(method -> {
            assertDoesNotThrow(() -> {
                CodeMetricsDiffResult codeMetricsDiffResult =
                    (CodeMetricsDiffResult) method.invoke(result);
                assertPropertiesOfCodeMetricsDiffResult(codeMetricsDiffResult);
            }, method.getName() + "Thrown Exception while invoking");
        });
    }

    private void assertPropertiesOfCodeMetricsDiffResult(
        CodeMetricsDiffResult codeMetricsDiffResult) {
        assertFalse(codeMetricsDiffResult.getFileName().contains(PATH_SEPARATOR));
        assertNotNull(codeMetricsDiffResult.getConstructType(),
            codeMetricsDiffResult.getConstructName() + "Came out Null instead of Construct type");
        assertNotNull(codeMetricsDiffResult.getFileName(),
            codeMetricsDiffResult.getConstructName() + "Came out Null instead of file name");
        assertNotNull(codeMetricsDiffResult.getConstructName(),
            codeMetricsDiffResult.getConstructName() + "Came out Null instead of Construct name");
        assertNotNull(codeMetricsDiffResult.getMetricName(),
            codeMetricsDiffResult.getMetricName() + "Came out Null instead of value");
        assertNotNull(codeMetricsDiffResult.getNewValue(),
            codeMetricsDiffResult.getMetricName() + "Came out Null for New Value ");
        assertNotNull(codeMetricsDiffResult.getOldValue(),
            codeMetricsDiffResult.getMetricName() + "Came out Null for Old value ");
    }

}
