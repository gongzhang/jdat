package cn.edu.buaa.sei.jdat.graph;

import java.util.HashMap;
import java.util.Map;

import cn.edu.buaa.sei.jdat.Utility;
import cn.edu.buaa.sei.jdat.model.Package;
import cn.edu.buaa.sei.jdat.model.Type;

public class PackageDependency implements IDependency<Package, Package> {
	
	private Package source;
	private Package destination;
	private Map<String, TypeDependency> details;
	
	@Override
	public Package getSource() {
		return this.source;
	}
	
	@Override
	public Package getDestination() {
		return destination;
	}
	
	public PackageDependency(Package source, Package dest) {
		this.source = source;
		this.destination = dest;
	}
	
	private void generateUnknownTypeDetails(DependencyGraph graph) {
		if (!graph.isOptionFlagged(DependencyGraph.OPT_UNKNOWN_PACKAGES))
			return;
		if (details == null) {
			Map<String, Type> unknownTypes = graph.getUnknownTypes();
			details = new HashMap<String, TypeDependency>();
			for (Type type : source.getTypes()) {
				for (String tref : type.getUnresolvedReferences()) {
					if (Utility.getPackageName(tref).equals(destination.getName())) {
						// add unknown type
						if (!unknownTypes.containsKey(tref))
							unknownTypes.put(tref, Type.createUnknownType(tref));
						
						Type refer = unknownTypes.get(tref);
						TypeDependency dependency = new TypeDependency(type, refer);
						if (type.isSubtypeOf(tref))
							dependency.kind = TypeDependency.INHERITANCE;
						details.put(DependencyGraph.getKey(type, refer), dependency);
					}
				}
			}
		}
	}
	
	private void generateDetails() {
		if (details == null) {
			details = new HashMap<String, TypeDependency>();
			for (Type type : source.getTypes()) {
				for (Type refer : type.getReferences()) {
					if (refer.getOwner() == destination) {
						TypeDependency dependency = new TypeDependency(type, refer);
						if (type.isSubtypeOf(refer)) {
							dependency.kind = TypeDependency.INHERITANCE;
						}
						details.put(DependencyGraph.getKey(type, refer), dependency);
					}
				}
			}
		}
	}
	
	public TypeDependency[] getDependencies(DependencyGraph graph) {
		if (details == null) {
			if (destination.isUnknown())
				generateUnknownTypeDetails(graph);
			else
				generateDetails();
		}
		return details.values().toArray(new TypeDependency[0]);
	}
	
	public TypeDependency getDependency(Type type, Type refer) {
		if (details == null)
			generateDetails();
		String key = DependencyGraph.getKey(type, refer);
		return details.get(key);
	}
}
