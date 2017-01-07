package FeatureModels;

import java.util.*;

import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Main {
  public static void Run() {

    new FeatureModelTest().main();
  }

  public Main() {}

  public String toString() {

    return "Main{}";
  }

  public static void main(String[] args) {
	Run();
    IO.println(Utils.toString(Utils.VOID_VALUE));
  }
}
