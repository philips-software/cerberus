package com.philips.swcoe.cerberus.cerebellum.codemetrics.java;

import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsClassResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsMethodResult;
import com.philips.swcoe.cerberus.cerebellum.codemetrics.java.results.CodeMetricsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import static com.philips.swcoe.cerberus.unit.test.utils.UnitTestConstants.*;
import static org.junit.jupiter.api.Assertions.*;

public class CodeMetricsDiffServiceTest {

    private String previousPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_PREVIOUS;
    private String currentPath = RESOURCES + PATH_SEPARATOR + TEST_JAVA_CODE_CURRENT;
    private String configPath = RESOURCES + PATH_SEPARATOR + "class_metrics_to_display.properties";
    private CodeMetricsDiffService codeMetricsDiffService;
    private List<CodeMetricsClassResult> codeMetricsClassResultList;
    private Function<String, CodeMetricsClassResult> javaCodeMetricsResultToTest =
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
        CodeMetricsClassResult result = codeMetricsClassResultList.stream().filter(javaCodeMetricsClassResult ->  javaCodeMetricsClassResult.getFile().contains("Rhombus.java"))
                .findFirst().get();
        assertEquals("Rhombus.java", result.getFile());
        assertEquals("Shapes.Rhombus", result.getClassName());
        assertDoesNotThrowForEachClassMetric(result);
    }

    @Test
    public void shouldSetResultForAllMetricsforNewlyIntroducedFile() {
        CodeMetricsClassResult resultToEvaluate = codeMetricsClassResultList.stream().filter(javaCodeMetricsClassResult ->  javaCodeMetricsClassResult.getFile().contains("Triangle.java"))
                .findFirst().get();
        assertDoesNotThrowForEachClassMetric(resultToEvaluate);
    }


    @Test
    public void shouldGetMetricsWhichWasNewlyCreatedInCurrent() throws Exception {
        Optional<CodeMetricsClassResult> result = codeMetricsClassResultList.stream().filter(javaCodeMetricsClassResult ->  javaCodeMetricsClassResult.getFile().contains("Triangle.java"))
                .findFirst();
        assertEquals("Triangle.java", result.get().getFile());
        assertEquals("Shapes.Triangle", result.get().getClassName());
        assertEquals(17, result.get().getLinesOfCode().getNewValue());
        assertEquals(4, result.get().getNumberOfMethods().getNewValue());
        assertEquals(4, result.get().getNumberOfPublicMethods().getNewValue());
        assertEquals(2, result.get().getReturnQty().getNewValue());
        assertEquals(0, result.get().getNumberOfProtectedMethods().getNewValue());
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
        assertEquals(27, javaCodeMetricsResultToTest.apply("Rectangle.java").getLinesOfCode().getOldValue());
        assertEquals(37, javaCodeMetricsResultToTest.apply("Rectangle.java").getLinesOfCode().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfMethods() throws Exception {
        assertEquals(7, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfMethods().getOldValue());
        assertEquals(10, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfMethods().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfPrivateProperties() throws Exception {
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfPrivateFields().getOldValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfPublicMethods() throws Exception {
        assertEquals(3, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfPublicMethods().getOldValue());
        assertEquals(5, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfPublicMethods().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfFields() throws Exception {
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfFields().getOldValue());
        assertEquals(3, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfFields().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNumberOfProtectedMethods() throws Exception {
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfProtectedMethods().getOldValue());
        assertEquals(3, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfProtectedMethods().getNewValue());
    }


    @Test
    public void metricsShouldHaveBothOldAndNewValuesForDepthInheritanceTree() throws Exception {
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getDepthInheritanceTree().getOldValue());
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getDepthInheritanceTree().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForWMC() throws Exception {
        assertEquals(7, javaCodeMetricsResultToTest.apply("Rectangle.java").getWeightMethodClass().getOldValue());
        assertEquals(10, javaCodeMetricsResultToTest.apply("Rectangle.java").getWeightMethodClass().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForCBO() throws Exception {
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getCouplingBetweenObjects().getOldValue());
        assertEquals(3, javaCodeMetricsResultToTest.apply("Rectangle.java").getCouplingBetweenObjects().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForRFC() throws Exception {
        assertEquals(4, javaCodeMetricsResultToTest.apply("Rectangle.java").getResponseForClass().getOldValue());
        assertEquals(2, javaCodeMetricsResultToTest.apply("Rectangle.java").getResponseForClass().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForNOSI() throws Exception {
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfStaticInvocations().getOldValue());
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getNumberOfStaticInvocations().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForRtnQuantity() throws Exception {
        assertEquals(4, javaCodeMetricsResultToTest.apply("Rectangle.java").getReturnQty().getOldValue());
        assertEquals(5, javaCodeMetricsResultToTest.apply("Rectangle.java").getReturnQty().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForComparisionQuantity() throws Exception {
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getComparisonsQty().getOldValue());
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getComparisonsQty().getNewValue());
    }

    @Test
    public void metricsShouldHaveBothOldAndNewValuesForTryCatchQty() throws Exception {
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getTryCatchQty().getOldValue());
        assertEquals(0, javaCodeMetricsResultToTest.apply("Rectangle.java").getTryCatchQty().getNewValue());
    }

    @Test
    public void metricsShouldHaveMethodLevelMetricsRecordedForEachFile() throws Exception {
        assertEquals(5, javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().size());
        assertEquals(7, javaCodeMetricsResultToTest.apply("Rectangle.java").getMethodMetrics().size());
        assertEquals(1, javaCodeMetricsResultToTest.apply("Main.java").getMethodMetrics().size());
        assertEquals(6, javaCodeMetricsResultToTest.apply("Shape.java").getMethodMetrics().size());
        assertEquals(7, javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().size());
    }

    @Test
    public void metricsShouldHaveMethodLevelMetricsSetToNotNull() throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult = javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(1);
        assertDoesNotThrowForEachMethodMetric(codeMetricsMethodResult);
    }


    @Test
    public void MethodLevelMetricsRecordedShouldHaveOldAndNewCyclometicComplexity() throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult = javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(0);
        assertEquals(1, codeMetricsMethodResult.getComplexity().getNewValue());
        assertEquals(1, codeMetricsMethodResult.getComplexity().getOldValue());
    }

    @Test
    public void MethodLevelMetricsRecordedShouldHaveOldAndNewCyclometicComplexityForNonExistantInCurrent() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        CodeMetricsMethodResult codeMetricsMethodResult = javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().get(0);
        assertEquals(0, codeMetricsMethodResult.getComplexity().getNewValue());
        assertEquals(1, codeMetricsMethodResult.getComplexity().getOldValue());
    }

    @Test
    public void MethodLevelMetricsRecordedShouldHaveOldAndNewLinesOfCode() throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult = javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(0);
        assertEquals(3, codeMetricsMethodResult.getLinesOfCode().getNewValue());
        assertEquals(3, codeMetricsMethodResult.getLinesOfCode().getOldValue());
    }

    @Test
    public void MethodLevelMetricsRecordedShouldHaveOldAndNewLinesOfCodeForNonExistantInCurrent() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        CodeMetricsMethodResult codeMetricsMethodResult = javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().get(0);
        assertEquals(0, codeMetricsMethodResult.getLinesOfCode().getNewValue());
        assertEquals(3, codeMetricsMethodResult.getLinesOfCode().getOldValue());
    }

    @Test
    public void MethodLevelMetricsRecordedShouldHaveOldAndNewStartLineNo() throws Exception {
        CodeMetricsMethodResult codeMetricsMethodResult = javaCodeMetricsResultToTest.apply("Circle.java").getMethodMetrics().get(0);
        assertEquals(6, codeMetricsMethodResult.getStartLineNo().getNewValue());
        assertEquals(6, codeMetricsMethodResult.getStartLineNo().getOldValue());
    }

    @Test
    public void MethodLevelMetricsRecordedShouldHaveOldAndNewStartLineNoForNonExistantInCurrent() throws Exception {
        List<CodeMetricsClassResult> codeMetricsClassResultList = codeMetricsDiffService.getMetricsFromSourceCode();
        CodeMetricsMethodResult codeMetricsMethodResult = javaCodeMetricsResultToTest.apply("Rhombus.java").getMethodMetrics().get(0);
        assertEquals(0, codeMetricsMethodResult.getStartLineNo().getNewValue());
        assertEquals(16, codeMetricsMethodResult.getStartLineNo().getOldValue());
    }

    private void assertDoesNotThrowForEachClassMetric(CodeMetricsClassResult result) {
        Stream<Method> methodStream = Arrays.stream(CodeMetricsClassResult.class.getMethods()).filter(method -> method.getReturnType().equals(CodeMetricsResult.class));
        methodStream.forEach(method -> {
            assertDoesNotThrow(() -> {
                CodeMetricsResult codeMetricsResult = (CodeMetricsResult) method.invoke(result);
                assertNotNull(codeMetricsResult.getMetricName(), codeMetricsResult.getMetricName() + "Came out Null instead of value");
                assertNotNull(codeMetricsResult.getNewValue(), codeMetricsResult.getMetricName() + "Came out Null for New Value ");
                assertNotNull(codeMetricsResult.getOldValue(), codeMetricsResult.getMetricName() + "Came out Null for Old value ");
            }, method.getName() + "Throwed Exception while invoking");
        });
    }

    private void assertDoesNotThrowForEachMethodMetric(CodeMetricsMethodResult result) {
        Stream<Method> methodStream = Arrays.stream(CodeMetricsMethodResult.class.getMethods()).filter(method -> method.getReturnType().equals(CodeMetricsResult.class));
        methodStream.forEach(method -> {
            assertDoesNotThrow(() -> {
                CodeMetricsResult codeMetricsResult = (CodeMetricsResult) method.invoke(result);
                assertNotNull(codeMetricsResult.getMetricName(), codeMetricsResult.getMetricName() + "Came out Null instead of value");
                assertNotNull(codeMetricsResult.getNewValue(), codeMetricsResult.getMetricName() + "Came out Null for New Value ");
                assertNotNull(codeMetricsResult.getOldValue(), codeMetricsResult.getMetricName() + "Came out Null for Old value ");
            }, method.getName() + "Throwed Exception while invoking");
        });
    }
}
