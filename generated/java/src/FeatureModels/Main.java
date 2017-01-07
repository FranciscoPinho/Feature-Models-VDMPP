package FeatureModels;

import java.util.*;

import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Main {
	public static void Run() {
		FeatureModel fm = new FeatureModel("root");
		fm.addMandatorySub("0", "root");
		fm.addXorSub("1", "root");
		fm.addXorSub("2", "root");
		fm.addOrSub("3", "root");
		fm.addOrSub("4", "root");
		fm.addOptionalSub("5", "root");
		fm.addOptionalSub("6", "5");
		fm.addXorSub("7", "0");
		fm.addXorSub("8", "0");
		fm.printModel(fm.get(fm.getRootName()),"root");
		// new FeatureModelTest().main();
	}

	public Main() {
	}

	public String toString() {

		return "Main{}";
	}

	public static void main(String[] args) {
		Run();
	}
}
