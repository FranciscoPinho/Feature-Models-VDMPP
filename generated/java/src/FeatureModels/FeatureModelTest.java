package FeatureModels;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class FeatureModelTest {
  private FeatureModel fm = new FeatureModel("root");
  private FeatureModel fm2 = new FeatureModel("E-shop");

  protected void assertTrue(final Boolean arg) {

    return;
  }

  protected void assertEqual(final Object expected, final Object actual) {

    if (!(Utils.equals(expected, actual))) {
      IO.print("Actual value (");
      IO.print(((Object) actual));
      IO.print(") different from expected (");
      IO.print(((Object) expected));
      IO.println(")\n");
    }
  }

  private void testMakeModel() {

    fm.addMandatorySub("0", "root");
    fm.addXorSub("1", "root");
    fm.addXorSub("2", "root");
    fm.addOrSub("3", "root");
    fm.addOrSub("4", "root");
    fm.addOptionalSub("5", "root");
    fm.addOptionalSub("6", "5");
    fm.addXorSub("7", "0");
    fm.addXorSub("8", "0");
    fm.requires("8", "3");
    fm.excludes("8", "4");
    assertTrue(Utils.equals(fm.get("root").getSubFeatures().size(), 6L));
  }

  private void testGenerateValidConfigs() {

    fm2.addMandatorySub("Payment", "E-shop");
    fm2.addMandatorySub("Catalogue", "E-shop");
    fm2.addMandatorySub("Security", "E-shop");
    fm2.addXorSub("High", "Security");
    fm2.addXorSub("Standard", "Security");
    fm2.addOrSub("Credit Card", "Payment");
    fm2.addOrSub("Bank Transfer", "Payment");
    fm2.addOptionalSub("Search", "E-shop");
    fm2.requires("High", "Credit Card");
    fm2.generateValidConfigs();
    if (Utils.equals(fm.allValidConfigurations.size(), 0L)) {
      IO.println("Error:Invalid Model, couldnt generate a single valid Configuration");
    } else {
      IO.println("|!|!|!|PRINTING ALL THE VALID CONFIGS|!|!|!|");
      fm2.printAllConfigurations();
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set(
                  "E-shop",
                  "Catalogue",
                  "Payment",
                  "Security",
                  "Credit Card",
                  "Bank Transfer",
                  "High")));
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set("E-shop", "Catalogue", "Payment", "Security", "Credit Card", "High")));
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set(
                  "E-shop", "Catalogue", "Payment", "Security", "Bank Transfer", "Standard")));
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set("E-shop", "Catalogue", "Payment", "Security", "Bank Transfer", "High")));
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set(
                  "E-shop",
                  "Catalogue",
                  "Payment",
                  "Security",
                  "Search",
                  "Credit Card",
                  "Bank Transfer",
                  "High")));
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set(
                  "E-shop", "Catalogue", "Payment", "Security", "Search", "Credit Card", "High")));
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set(
                  "E-shop",
                  "Catalogue",
                  "Payment",
                  "Security",
                  "Search",
                  "Bank Transfer",
                  "Standard")));
      assertTrue(
          fm2.makeConfiguration(
              SetUtil.set(
                  "E-shop",
                  "Catalogue",
                  "Payment",
                  "Security",
                  "Search",
                  "Bank Transfer",
                  "High")));
      assertEqual(8L, fm2.allValidConfigurations.size());
      IO.println("|!|!|!|END PRINTING ALL THE VALID CONFIGS|!|!|!|");
    }
  }

  private void testRemoveRequiresExcludes() {

    fm.addMandatorySub("0", "root");
    fm.addXorSub("1", "root");
    fm.addXorSub("2", "root");
    fm.addOrSub("3", "root");
    fm.addOrSub("4", "root");
    fm.addOptionalSub("5", "root");
    fm.addOptionalSub("6", "5");
    fm.addXorSub("7", "0");
    fm.addXorSub("8", "0");
    fm.requires("8", "3");
    fm.excludes("8", "4");
    fm.removeRequires("8", "3");
    fm.removeExcludes("8", "4");
    fm.generateValidConfigs();
    if (Utils.equals(fm.allValidConfigurations.size(), 0L)) {
      IO.println("Error:Invalid Model, couldnt generate a single valid Configuration");
    } else {
      assertEqual(36L, fm.allValidConfigurations.size());
    }
  }

  private void testMakeConfiguration() {

    fm.addMandatorySub("0", "root");
    fm.addXorSub("1", "root");
    fm.addXorSub("2", "root");
    fm.addOrSub("3", "root");
    fm.addOrSub("4", "root");
    fm.addOptionalSub("5", "root");
    fm.generateValidConfigs();
    if (Utils.equals(fm.allValidConfigurations.size(), 0L)) {
      IO.println("Error:Invalid Model, couldnt generate a single valid Configuration");
    } else {
      assertEqual(12L, fm.allValidConfigurations.size());
    }

    assertTrue(fm.makeConfiguration(SetUtil.set("root", "0", "1", "3", "5")));
    assertTrue(fm.makeConfiguration(SetUtil.set("root", "0", "1", "3")));
    assertTrue(!(fm.makeConfiguration(SetUtil.set("root", "0", "1", "2"))));
  }

  private void testIndegreeCalculation() {

    fm.addMandatorySub("1", "root");
    fm.addXorSub("1.2", "1");
    fm.addOptionalSub("1.2.3", "1.2");
    fm.addOrSub("1.2.3.4", "1.2.3");
    assertTrue(Utils.equals(fm.get("1.2.3.4").indegree, 4L));
    assertTrue(Utils.equals(fm.get("1.2.3").indegree, 3L));
    assertTrue(Utils.equals(fm.get("1.2").indegree, 2L));
    assertTrue(Utils.equals(fm.get("1").indegree, 1L));
    assertTrue(Utils.equals(fm.get("root").indegree, 0L));
  }

  private void testCyclicModel() {

    fm.addMandatorySub("1", "root");
    fm.addMandatorySub("root", "root");
  }

  private void testMakeInvalidConfiguration() {

    fm.addMandatorySub("0", "root");
    fm.addXorSub("1", "root");
    fm.addXorSub("2", "root");
    fm.addOrSub("3", "root");
    fm.addOrSub("4", "root");
    fm.addOptionalSub("5", "root");
    fm.generateValidConfigs();
    if (Utils.equals(fm.allValidConfigurations.size(), 0L)) {
      IO.println("Error:Invalid Model, couldnt generate a single valid Configuration");
    } else {
      assertEqual(12L, fm.allValidConfigurations.size());
    }

    assertTrue(fm.makeConfiguration(SetUtil.set("root", "0", "1", "2")));
  }

  private void testSelectNonExistant() {

    fm.addMandatorySub("1", "root");
    IO.println(fm.get("2").name);
  }

  private void testExcludeSubFeature() {

    fm.addMandatorySub("0", "root");
    fm.addXorSub("1", "root");
    fm.addXorSub("2", "root");
    fm.addOrSub("3", "root");
    fm.addOrSub("4", "root");
    fm.excludes("2", "root");
  }

  public static void main() {
	 	new FeatureModelTest().testIndegreeCalculation();
    new FeatureModelTest().testMakeModel();
    new FeatureModelTest().testGenerateValidConfigs();
    new FeatureModelTest().testRemoveRequiresExcludes();
    new FeatureModelTest().testMakeConfiguration();
  }

  public FeatureModelTest() {}

  public String toString() {

    return "FeatureModelTest{"
        + "fm := "
        + Utils.toString(fm)
        + ", fm2 := "
        + Utils.toString(fm2)
        + "}";
  }
}
