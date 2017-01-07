package FeatureModels;

import java.util.*;

import javax.swing.JTextArea;

import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class FeatureModel {
  public VDMSet featureTree = SetUtil.set();
  private String rootName;
  public VDMSet allValidConfigurations = SetUtil.set(SetUtil.set());

  public void cg_init_FeatureModel_1(final String root) {

    addFeature(root);
    rootName = root;
    return;
  }

  public FeatureModel(final String root) {

    cg_init_FeatureModel_1(root);
  }

  public Feature get(final String fname) {

    for (Iterator iterator_16 = featureTree.iterator(); iterator_16.hasNext(); ) {
      Feature f = (Feature) iterator_16.next();
      if (Utils.equals(f.name, fname)) {
        return f;
      }
    }
    return null;
  }

  public void addFeature(final String fname) {
    Feature f = new Feature(fname);
    featureTree = SetUtil.union(Utils.copy(featureTree), SetUtil.set(f));
  }

  public void addMandatorySub(final String newF, final String targetF) {

    addFeature(newF);
    get(targetF).addMandatory(get(newF));
    get(newF).setParent(targetF);
    calcIndegree(get(newF));
  }

  public void addOptionalSub(final String newF, final String targetF) {

    addFeature(newF);
    get(targetF).addOptional(get(newF));
    get(newF).setParent(targetF);
    calcIndegree(get(newF));
  }

  public void addOrSub(final String newF, final String targetF) {

    addFeature(newF);
    get(targetF).addOr(get(newF));
    get(newF).setParent(targetF);
    calcIndegree(get(newF));
  }

  public void addXorSub(final String newF, final String targetF) {

    addFeature(newF);
    get(targetF).addXor(get(newF));
    get(newF).setParent(targetF);
    calcIndegree(get(newF));
  }

  public void requires(final String requiree, final String requirer) {

    get(requirer).addRequire(get(requiree));
  }

  public void excludes(final String excludee, final String excluder) {
    if (!(SetUtil.inSet(get(excludee), get(rootName).mandatory))) {
      get(excluder).addExclude(get(excludee));
    }
  }

  public void removeExcludes(final String excludee, final String excluder) {

    if (SetUtil.inSet(get(excludee), get(excluder).excludes)) {
      get(excluder).removeExclude(get(excludee));
    } else {
      return;
    }
  }

  public void removeRequires(final String requiree, final String requirer) {

    if (SetUtil.inSet(get(requiree), get(requirer).requires)) {
      get(requirer).removeRequire(get(requiree));
    } else {
      return;
    }
  }

  public void calcIndegree(final Feature feature) {

    if (Utils.equals(feature.parent, rootName)) {
      feature.incIndegree();
    } else {
      calcIndegreeAux(get(feature.parent), feature);
    }
  }

  public void calcIndegreeAux(final Feature parentF, final Feature original) {

    original.incIndegree();
    if (Utils.equals(parentF.parent, rootName)) {
      original.incIndegree();
    } else {
      calcIndegreeAux(get(parentF.parent), original);
    }
  }

  public void generateValidConfigs() {

    VDMSet currentConfigs = SetUtil.set(SetUtil.set(get(rootName)));
    VDMSet orGroupOptions = SetUtil.set(SetUtil.set());
    VDMSet xorGroupOptions = SetUtil.set(SetUtil.set());
    VDMSet optionalOptions = SetUtil.set(SetUtil.set());
    allValidConfigurations = SetUtil.set(SetUtil.set());
    if (!(Utils.equals(get(rootName).xorGroup.size(), 0L))) {
      for (Iterator iterator_17 = get(rootName).xorGroup.iterator(); iterator_17.hasNext(); ) {
        Feature f = (Feature) iterator_17.next();
        xorGroupOptions = SetUtil.union(Utils.copy(xorGroupOptions), SetUtil.set(SetUtil.set(f)));
      }
      xorGroupOptions = SetUtil.diff(Utils.copy(xorGroupOptions), SetUtil.set(SetUtil.set()));
      currentConfigs =
          combinePossibilities(Utils.copy(currentConfigs), Utils.copy(xorGroupOptions));
    }

    if (!(Utils.equals(get(rootName).orGroup.size(), 0L))) {
      orGroupOptions = SetUtil.powerset(get(rootName).orGroup);
      orGroupOptions = SetUtil.diff(Utils.copy(orGroupOptions), SetUtil.set(SetUtil.set()));
      currentConfigs = combinePossibilities(Utils.copy(currentConfigs), Utils.copy(orGroupOptions));
    }

    if (!(Utils.equals(get(rootName).mandatory.size(), 0L))) {
    	 currentConfigs = uniteSets(Utils.copy(currentConfigs), get(rootName).mandatory);
    }

    if (!(Utils.equals(get(rootName).optional.size(), 0L))) {
      optionalOptions = SetUtil.powerset(get(rootName).optional);
      currentConfigs =
          combinePossibilities(Utils.copy(currentConfigs), Utils.copy(optionalOptions));
    }

    for (Iterator iterator_19 = currentConfigs.iterator(); iterator_19.hasNext(); ) {
      VDMSet config = (VDMSet) iterator_19.next();
      VDMSet mustRequire = SetUtil.set();
      VDMSet mustProcess = SetUtil.set();
      for (Iterator iterator_20 = config.iterator(); iterator_20.hasNext(); ) {
        Feature f = (Feature) iterator_20.next();
        if (!(Utils.equals(f.name, rootName))) {
          if (f.requires.size() > 0L) {
            mustRequire = SetUtil.union(Utils.copy(mustRequire), f.requires);
          }

          if (f.getSubFeatures().size() > 0L) {
            mustProcess = SetUtil.union(Utils.copy(mustProcess), SetUtil.set(f));
          }
        }
      }
      generateValidConfigsAux(Utils.copy(config), Utils.copy(mustRequire), Utils.copy(mustProcess));
    }
  }

  private void generateValidConfigsAux(
      final VDMSet config, final VDMSet mustRequire, final VDMSet mustProcess) {

    VDMSet currentConfigs = SetUtil.set(SetUtil.set());
    Number firstProcess = 0L;
    if (checkConstraints(Utils.copy(config), Utils.copy(mustRequire), Utils.copy(mustProcess))) {
      allValidConfigurations =
          SetUtil.union(Utils.copy(allValidConfigurations), SetUtil.set(Utils.copy(config)));
      allValidConfigurations =
          SetUtil.diff(Utils.copy(allValidConfigurations), SetUtil.set(SetUtil.set()));
      return;
    }

    if (Utils.equals(mustProcess.size(), 0L)) {
      return;
    }

    for (Iterator iterator_21 = mustProcess.iterator(); iterator_21.hasNext(); ) {
      Feature p = (Feature) iterator_21.next();
      if (Utils.equals(firstProcess, 0L)) {
        currentConfigs =
            SetUtil.union(
                Utils.copy(currentConfigs), generateValidConfigsCompute(Utils.copy(config), p));
        currentConfigs = SetUtil.diff(Utils.copy(currentConfigs), SetUtil.set(SetUtil.set()));

      } else {
        currentConfigs =
            combinePossibilities(
                Utils.copy(currentConfigs), generateValidConfigsCompute(Utils.copy(config), p));
        currentConfigs = SetUtil.diff(Utils.copy(currentConfigs), SetUtil.set(SetUtil.set()));
      }

      firstProcess = firstProcess.longValue() + 1L;
    }
    for (Iterator iterator_22 = currentConfigs.iterator(); iterator_22.hasNext(); ) {
      VDMSet conf = (VDMSet) iterator_22.next();
      VDMSet newMustRequire = SetUtil.set();
      VDMSet newMustProcess = SetUtil.set();
      for (Iterator iterator_23 = conf.iterator(); iterator_23.hasNext(); ) {
        Feature f = (Feature) iterator_23.next();
        if (!(Utils.equals(f.name, rootName))) {
          Boolean andResult_13 = false;

          if (f.requires.size() > 0L) {
            if (!(SetUtil.inSet(f, mustRequire))) {
              andResult_13 = true;
            }
          }

          if (andResult_13) {
            newMustRequire = SetUtil.union(Utils.copy(newMustRequire), f.requires);
          }

          Boolean andResult_14 = false;

          if (f.getSubFeatures().size() > 0L) {
            if (!(SetUtil.inSet(f, mustProcess))) {
              andResult_14 = true;
            }
          }

          if (andResult_14) {
            newMustProcess = SetUtil.union(Utils.copy(newMustProcess), SetUtil.set(f));
          }
        }

        generateValidConfigsAux(
            Utils.copy(conf),
            SetUtil.union(Utils.copy(newMustRequire), Utils.copy(mustRequire)),
            Utils.copy(newMustProcess));
      }
    }
  }

  private VDMSet generateValidConfigsCompute(final VDMSet conf, final Feature toProcess) {

    VDMSet currentConfigs = SetUtil.set(Utils.copy(conf));
    VDMSet orGroupOptions = SetUtil.set(SetUtil.set());
    VDMSet xorGroupOptions = SetUtil.set(SetUtil.set());
    VDMSet optionalOptions = SetUtil.set(SetUtil.set());
    if (!(Utils.equals(toProcess.xorGroup.size(), 0L))) {
      for (Iterator iterator_24 = toProcess.xorGroup.iterator(); iterator_24.hasNext(); ) {
        Feature f = (Feature) iterator_24.next();
        xorGroupOptions = SetUtil.union(Utils.copy(xorGroupOptions), SetUtil.set(SetUtil.set(f)));
      }
      xorGroupOptions = SetUtil.diff(Utils.copy(xorGroupOptions), SetUtil.set(SetUtil.set()));
      currentConfigs =
          combinePossibilities(Utils.copy(currentConfigs), Utils.copy(xorGroupOptions));
    }

    if (!(Utils.equals(toProcess.orGroup.size(), 0L))) {
      orGroupOptions = SetUtil.powerset(toProcess.orGroup);
      orGroupOptions = SetUtil.diff(Utils.copy(orGroupOptions), SetUtil.set(SetUtil.set()));
      currentConfigs = combinePossibilities(Utils.copy(currentConfigs), Utils.copy(orGroupOptions));
    }

    if (!(Utils.equals(toProcess.mandatory.size(), 0L))) {
    	 currentConfigs = uniteSets(Utils.copy(currentConfigs), toProcess.mandatory);
    }

    if (!(Utils.equals(toProcess.optional.size(), 0L))) {
      optionalOptions = SetUtil.powerset(toProcess.optional);
      currentConfigs =
          combinePossibilities(Utils.copy(currentConfigs), Utils.copy(optionalOptions));
    }

    return Utils.copy(currentConfigs);
  }

  public Boolean checkConstraints(
      final VDMSet config, final VDMSet mustRequire, final VDMSet mustProcess) {

    if (mustProcess.size() > 0L) {
      return false;
    }

    if (!(SetUtil.subset(mustRequire, config))) {
      return false;
    }

    for (Iterator iterator_26 = config.iterator(); iterator_26.hasNext(); ) {
      Feature f = (Feature) iterator_26.next();
      if (!(Utils.empty(SetUtil.intersect(f.excludes, Utils.copy(config))))) {
        return false;
      }
    }
    return true;
  }

  public VDMSet combinePossibilities(final VDMSet poss1, final VDMSet poss2) {

    VDMSet retValue = SetUtil.set(SetUtil.set());
    for (Iterator iterator_27 = poss1.iterator(); iterator_27.hasNext(); ) {
      VDMSet ps = (VDMSet) iterator_27.next();
      for (Iterator iterator_28 = poss2.iterator(); iterator_28.hasNext(); ) {
        VDMSet ps2 = (VDMSet) iterator_28.next();
        retValue =
            SetUtil.union(
                Utils.copy(retValue), SetUtil.set(SetUtil.union(Utils.copy(ps), Utils.copy(ps2))));
      }
    }
    retValue = SetUtil.diff(Utils.copy(retValue), SetUtil.set(SetUtil.set()));
    return Utils.copy(retValue);
  }

  public VDMSet uniteSets(final VDMSet s1, final VDMSet s2) {

	    VDMSet retValue = SetUtil.set(SetUtil.set());
	    for (Iterator iterator_24 = s1.iterator(); iterator_24.hasNext(); ) {
	      VDMSet ps = (VDMSet) iterator_24.next();
	      retValue =
	          SetUtil.union(
	              Utils.copy(retValue), SetUtil.set(SetUtil.union(Utils.copy(ps), Utils.copy(s2))));
	    }
	    retValue = SetUtil.diff(Utils.copy(retValue), SetUtil.set(SetUtil.set()));
	    return Utils.copy(retValue);
  }
	  
	  
  public Boolean makeConfiguration(final VDMSet list) {

    VDMSet getFeaturesFromName = SetUtil.set();
    for (Iterator iterator_29 = list.iterator(); iterator_29.hasNext(); ) {
      String e = (String) iterator_29.next();
      getFeaturesFromName = SetUtil.union(Utils.copy(getFeaturesFromName), SetUtil.set(get(e)));
    }
    if (SetUtil.inSet(getFeaturesFromName, allValidConfigurations)) {
      return true;

    } else {
      return false;
    }
  }

  public void printConfiguration(final VDMSet config) {

    IO.println("-----Current Configuration-----");
    for (Iterator iterator_30 = config.iterator(); iterator_30.hasNext(); ) {
      Feature f = (Feature) iterator_30.next();
      IO.println(f.name);
    }
    IO.println("-----End of Current Configuration-----");
  }

  public void printCombinations(final VDMSet configs) {

    for (Iterator iterator_31 = configs.iterator(); iterator_31.hasNext(); ) {
      VDMSet c = (VDMSet) iterator_31.next();
      printConfiguration(Utils.copy(c));
    }
  }

  public void printAllConfigurations() {

    for (Iterator iterator_32 = allValidConfigurations.iterator(); iterator_32.hasNext(); ) {
      VDMSet f = (VDMSet) iterator_32.next();
      printConfiguration(Utils.copy(f));
    }
  }
  public String getRootName() {
	  return rootName;
  }

  public FeatureModel() {}

  public String toString() {

    return "FeatureModel{"
        + "featureTree := "
        + Utils.toString(featureTree)
        + ", rootName := "
        + Utils.toString(rootName)
        + ", allValidConfigurations := "
        + Utils.toString(allValidConfigurations)
        + "}";
  }
  
  public void printModel(Feature f,String Type, JTextArea consoleText) {
	  printModelAux(f,Type,consoleText);
	  if(f.mandatory.size()>0){
		  for(Iterator iterator_32 = f.mandatory.iterator(); iterator_32.hasNext(); ) 
		  {
		      Feature sf = (Feature) iterator_32.next();
		      printModel(sf,"mandatory",consoleText);
		  
		  }
	  }
	  if(f.xorGroup.size()>0){
		  for(Iterator iterator_32 = f.xorGroup.iterator(); iterator_32.hasNext(); ) 
		  {
		      Feature sf = (Feature) iterator_32.next();
		      printModel(sf,"xor",consoleText);
		  
		  }
	  }
	  if(f.orGroup.size()>0){
		  for(Iterator iterator_32 = f.orGroup.iterator(); iterator_32.hasNext(); ) 
		  {
		      Feature sf = (Feature) iterator_32.next();
		      printModel(sf,"or",consoleText);
		  
		  }
	  }
	  if(f.optional.size()>0){
		  for(Iterator iterator_32 = f.optional.iterator(); iterator_32.hasNext(); ) 
		  {
		      Feature sf = (Feature) iterator_32.next();
		      printModel(sf,"optional",consoleText);
		  
		  }
	  }
  }
  
  public void printModelAux(Feature f,String type,JTextArea ct){
	  String treeSeparator = "";
	  for(int i=0;i<f.indegree.intValue();i++){
		  	treeSeparator=treeSeparator + "    ";
	      }
	  switch(type){
	  case "mandatory":
		  ct.setText(ct.getText()+treeSeparator+"(M)"+f.name+"\n");  
		  break;
	  case "optional":
		  ct.setText(ct.getText()+treeSeparator+"(O)"+f.name+"\n");   
		  break;
	  case "xor":
		  ct.setText(ct.getText()+treeSeparator+"(Xor)"+f.name+"\n"); 
		  break;
	  case "or":
		  ct.setText(ct.getText()+treeSeparator+"(Or)"+f.name+"\n"); 
		  break;
	  case "root":
		  ct.setText(ct.getText()+"(R)"+f.name+"\n");
	  default:
		  break;
	  }
	  
  }
  
  public void printConstraints(Feature f,JTextArea consoleText) {
	  
	  if(f.requires.size()>0){
		  for(Iterator iterator_32 = f.requires.iterator(); iterator_32.hasNext(); ) 
		  {
		      Feature sf = (Feature) iterator_32.next();
		      printConstraintsAux(f,sf,"requires",consoleText);
		  
		  }
	  }
	  if(f.excludes.size()>0){
		  for(Iterator iterator_32 = f.excludes.iterator(); iterator_32.hasNext(); ) 
		  {
		      Feature sf = (Feature) iterator_32.next();
		      printConstraintsAux(f,sf,"excludes",consoleText);
		  }
	  }
	  
	  if(f.getSubFeatures().size()>0){
		  for(Iterator iterator_32 = f.getSubFeatures().iterator(); iterator_32.hasNext(); ) 
		  {
		      Feature sf = (Feature) iterator_32.next();
		      printConstraints(sf,consoleText);
		  
		  }
	  }
	
  }
  
  public void printConstraintsAux(Feature f,Feature f2,String type,JTextArea ct){
	  	if(type=="requires")
		  ct.setText(ct.getText()+f.name+" requires "+ f2.name+"\n"); 
	  	if(type=="excludes")
	  	  ct.setText(ct.getText()+f.name+" excludes "+ f2.name+"\n"); 
  }
  
  public void javaPrintConfiguration(final VDMSet config,JTextArea ct) {
	  	String c="";
	    for (Iterator iterator_30 = config.iterator(); iterator_30.hasNext(); ) {
	      Feature f = (Feature) iterator_30.next();
	      c=c+f.name+" ";
	    }
	    ct.setText(ct.getText()+"Configuration: "+c+"\n");
  }

  public void javaPrintAllConfigurations(JTextArea ct) {
	  ct.setText(ct.getText()+"\n Printing all Valid Configurations \n");
	  for (Iterator iterator_32 = allValidConfigurations.iterator(); iterator_32.hasNext(); ) {
	      VDMSet f = (VDMSet) iterator_32.next();
	      javaPrintConfiguration(Utils.copy(f),ct);
	    }
	  ct.setText(ct.getText()+"\n ------------------------------------- \n");
  }
}
