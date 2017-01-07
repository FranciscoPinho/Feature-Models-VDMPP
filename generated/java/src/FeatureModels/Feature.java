package FeatureModels;

import java.util.*;
import org.overture.codegen.runtime.*;

@SuppressWarnings("all")
public class Feature {
  public String name;
  public String parent = "";
  public Number indegree = 0L;
  public VDMSet optional = SetUtil.set();
  public VDMSet mandatory = SetUtil.set();
  public VDMSet orGroup = SetUtil.set();
  public VDMSet xorGroup = SetUtil.set();
  public VDMSet requires = SetUtil.set();
  public VDMSet excludes = SetUtil.set();
  private Feature true_self;

  public void cg_init_Feature_1(final String nm) {

    name = nm;
    true_self = this;
    return;
  }

  public Feature(final String nm) {

    cg_init_Feature_1(nm);
  }

  public VDMSet getSubFeatures() {

    return SetUtil.union(
        SetUtil.union(
            SetUtil.union(Utils.copy(mandatory), Utils.copy(optional)), Utils.copy(orGroup)),
        Utils.copy(xorGroup));
  }

  public void setParent(final String pname) {

    parent = pname;
  }

  public void incIndegree() {

    indegree = indegree.longValue() + 1L;
  }

  public Boolean notRequiredExcluded(final Feature f) {

    Boolean forAllExpResult_1 = true;
    VDMSet set_1 = SetUtil.union(Utils.copy(requires), Utils.copy(excludes));
    for (Iterator iterator_1 = set_1.iterator(); iterator_1.hasNext() && forAllExpResult_1; ) {
      Feature f2 = ((Feature) iterator_1.next());
      forAllExpResult_1 = !(Utils.equals(f.name, f2.name));
    }
    return forAllExpResult_1;
  }

  public Boolean notSubFeature(final Feature f) {

    Boolean forAllExpResult_2 = true;
    VDMSet set_2 = this.getSubFeatures();
    for (Iterator iterator_2 = set_2.iterator(); iterator_2.hasNext() && forAllExpResult_2; ) {
      Feature f2 = ((Feature) iterator_2.next());
      forAllExpResult_2 = !(Utils.equals(f.name, f2.name));
    }
    return forAllExpResult_2;
  }

  public void addMandatory(final Feature newF) {

    Boolean andResult_1 = false;

    if (notRequiredExcluded(newF)) {
      if (notSubFeature(newF)) {
        andResult_1 = true;
      }
    }

    if (andResult_1) {
      mandatory = SetUtil.union(Utils.copy(mandatory), SetUtil.set(newF));
    }
  }

  public void addOptional(final Feature newF) {

    Boolean andResult_2 = false;

    if (notRequiredExcluded(newF)) {
      if (notSubFeature(newF)) {
        andResult_2 = true;
      }
    }

    if (andResult_2) {
      optional = SetUtil.union(Utils.copy(optional), SetUtil.set(newF));
    }
  }

  public void addOr(final Feature newF) {

    Boolean andResult_3 = false;

    if (notRequiredExcluded(newF)) {
      if (notSubFeature(newF)) {
        andResult_3 = true;
      }
    }

    if (andResult_3) {
      orGroup = SetUtil.union(Utils.copy(orGroup), SetUtil.set(newF));
    }
  }

  public void addXor(final Feature newF) {

    Boolean andResult_4 = false;

    if (notRequiredExcluded(newF)) {
      if (notSubFeature(newF)) {
        andResult_4 = true;
      }
    }

    if (andResult_4) {
      xorGroup = SetUtil.union(Utils.copy(xorGroup), SetUtil.set(newF));
    }
  }

  public void addRequire(final Feature newRequires) {

    if (notSubFeature(newRequires)) {
      if (SetUtil.inSet(newRequires, excludes)) {
        excludes = SetUtil.diff(Utils.copy(excludes), SetUtil.set(newRequires));
      }

      requires = SetUtil.union(Utils.copy(requires), SetUtil.set(newRequires));
    }
  }

  public void addExclude(final Feature newExclude) {

    if (notSubFeature(newExclude)) {
      if (SetUtil.inSet(newExclude, requires)) {
        requires = SetUtil.diff(Utils.copy(requires), SetUtil.set(newExclude));
      }

      excludes = SetUtil.union(Utils.copy(excludes), SetUtil.set(newExclude));
    }
  }

  public void removeExclude(final Feature cancelExclusion) {

    excludes = SetUtil.diff(Utils.copy(excludes), SetUtil.set(cancelExclusion));
  }

  public void removeRequire(final Feature cancelRequire) {

    requires = SetUtil.diff(Utils.copy(requires), SetUtil.set(cancelRequire));
  }

  public Feature() {}

  public String toString() {

    return "Feature{"
        + "name := "
        + Utils.toString(name)
        + ", parent := "
        + Utils.toString(parent)
        + ", indegree := "
        + Utils.toString(indegree)
        + ", optional := "
        + Utils.toString(optional)
        + ", mandatory := "
        + Utils.toString(mandatory)
        + ", orGroup := "
        + Utils.toString(orGroup)
        + ", xorGroup := "
        + Utils.toString(xorGroup)
        + ", requires := "
        + Utils.toString(requires)
        + ", excludes := "
        + Utils.toString(excludes)
        + ", true_self := "
        + Utils.toString(true_self)
        + "}";
  }
}
